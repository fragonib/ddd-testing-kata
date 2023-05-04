import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    groovy
    kotlin("plugin.spring")          // SpringBoot visibility (open) over Kotlin classes
    id("org.springframework.boot")   // SpringBoot task to manage project
    id("com.patdouble.cucumber-jvm") // Functional tests with Cucumber
    id("au.com.dius.pact")           // Contract tests with Pact
}

ext {
    set("kotlin.version", "1.8.20")
    set("groovyVersion", "4.0.5")
    set("spockVersion", "2.3-groovy-4.0")
    set("byteBuddyVersion", "1.12.17")
    set("objenesisVersion", "3.3")
    set("cucumberVersion", "6.10.0")
    set("assertjVersion", "3.11.1")
    set("jsonUnitVersion", "2.24.0")
    set("pactVersion", "4.5.6")
}

dependencyManagement {
    val springCloudVersion: String by project
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

dependencies {

    // Modules
    implementation(project(":shared"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

}

testing {

    suites {

        val applySpockBasedTestingDependencies = { suite: JvmTestSuite ->

            suite.dependencies {

                implementation(project())
                implementation(project(":shared"))

                // JUnit 5
                val assertjVersion: String by project.ext
                val jsonUnitVersion: String by project.ext
                implementation("org.assertj:assertj-core:$assertjVersion")
                implementation("net.javacrumbs.json-unit:json-unit-assertj:$jsonUnitVersion")

                // Spock
                val groovyVersion: String by project.ext
                val spockVersion: String by project.ext
                val byteBuddyVersion: String by project.ext
                val objenesisVersion: String by project.ext
                implementation(platform("org.apache.groovy:groovy-bom:$groovyVersion"))
                implementation("org.apache.groovy:groovy")
                implementation(platform("org.spockframework:spock-bom:$spockVersion"))
                implementation("org.spockframework:spock-core:$spockVersion")
                runtimeOnly("net.bytebuddy:byte-buddy:$byteBuddyVersion") // allows mocking of classes in addition to interfaces
                runtimeOnly("org.objenesis:objenesis:$objenesisVersion")  // allows mocking of classes without default constructor (together with ByteBuddy or CGLIB)
                runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.63.Final")

                // SpringBoot Test
                implementation("org.springframework.boot:spring-boot-starter-test")
                implementation("io.projectreactor:reactor-test")

            }
        }

        // Unit tests suite
        val test by getting(JvmTestSuite::class) {

            useJUnitJupiter()

            applySpockBasedTestingDependencies(this)

            targets {
                all {
                    testTask.configure {
                        description = "Fast-running unit tests."
                        filter { excludeTestsMatching("*IT") }
                    }
                }
            }

        }

        // Integration tests suite
        val integrationTest by registering(JvmTestSuite::class) {

            useJUnitJupiter()

            applySpockBasedTestingDependencies(this)

            // In this project ITs doesn't have indenpendent source folder, they´re collocated with unit test and
            // distinguished by the name of the class (ending with IT)
            sources {
                groovy {
                    setSrcDirs(listOf("src/test/groovy"))
                }
            }

            targets {
                all {
                    testTask.configure {
                        description = "Slow-running integration tests."
                        filter { includeTestsMatching("*IT") }
                        shouldRunAfter(test)
                    }
                }
            }

        }

        // Contract tests suite
        val contractTest by registering(JvmTestSuite::class) {

            applySpockBasedTestingDependencies(this)

            dependencies {
                val pactVersion: String by project.ext
                implementation(project(":shared"))
                implementation("au.com.dius.pact.consumer:junit5:$pactVersion")
                implementation("au.com.dius.pact.provider:spring:$pactVersion")
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                        filter {
                            isFailOnNoMatchingTests = false
                        }
                        testLogging {
                            exceptionFormat = FULL
                            showExceptions = true
                            showStandardStreams = true
                            events(PASSED, FAILED, SKIPPED, STANDARD_OUT, STANDARD_ERROR)
                        }
                    }
                }
            }
        }

    }

}

cucumber {
    suite("functionalTest")
    maxParallelForks = 1
    featureRoots = listOf(
        "features"
    )
    stepDefinitionRoots = listOf(
        "clean.the.forest.area.functional"
    )
    plugins = listOf(
        "pretty",
        "clean.the.forest.shared.testing.functional.WireMockLifeCycle",
        "clean.the.forest.shared.testing.functional.AppContainerLifeCycle"
    )
    ignoreFailures = false

    dependencies {
        val cucumberVersion: String by project.ext
        val assertjVersion: String by project.ext
        val jsonUnitVersion: String by project.ext
        val functionalTestImplementation by configurations
        functionalTestImplementation(project(":shared"))
        functionalTestImplementation("io.cucumber:cucumber-java8:$cucumberVersion")
        functionalTestImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
        functionalTestImplementation("io.cucumber:cucumber-picocontainer:$cucumberVersion")
        functionalTestImplementation("org.assertj:assertj-core:$assertjVersion")
        functionalTestImplementation("com.jayway.jsonpath:json-path")
        functionalTestImplementation("net.javacrumbs.json-unit:json-unit-assertj:$jsonUnitVersion")
    }

}

pact {

    broker {
        pactBrokerUrl = "https://your-broker-url/"

        // To use basic auth
        pactBrokerUsername = "<USERNAME>"
        pactBrokerPassword = "<PASSWORD>"

        // OR to use a bearer token
        pactBrokerToken = "<TOKEN>"

        // Customise the authentication header from the default `Authorization`
    }

}

tasks {

    jar {
        enabled = true
    }

    bootJar {
        enabled = false
        archiveClassifier.set("boot")
    }

    java {
        withSourcesJar()
    }

    check {
        if (project.hasProperty("integrationTests")) {
            dependsOn(testing.suites.named("integrationTest"))
        }
    }

    withType<Delete> {
        doFirst {
            delete("~/.m2/repository/clean/the/forest/clean-the-forest-gradle")
        }
    }

}
