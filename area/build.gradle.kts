import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5
import org.springframework.cloud.contract.verifier.config.TestMode.EXPLICIT

plugins {
    groovy
    kotlin("plugin.spring")          // SpringBoot visibility (open) over Kotlin classes
    id("org.springframework.boot")   // SpringBoot task to manage project
    id("org.springframework.cloud.contract") // Contract verifier tasks
    id("maven-publish")                      // To publish contract artifacts
    id("com.patdouble.cucumber-jvm") // Functional tests with Cucumber
}

ext {
    set("kotlin.version", "1.8.20")
    set("groovyVersion", "4.0.5")
    set("spockVersion", "2.3-groovy-4.0")
    set("byteBuddyVersion", "1.12.17")
    set("objenesisVersion", "3.3")
    set("cucumberVersion", "6.10.0")
    set("springCloudVersion", "2020.0.3")
    set("assertjVersion", "3.11.1")
    set("jsonUnitVersion", "2.24.0")
}

dependencyManagement {
    val springCloudVersion: String by project
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

contracts {
    testFramework.set(JUNIT5)
    testMode.set(EXPLICIT)
    packageWithBaseClasses.set("clean.the.forest.area.contract")
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
}

dependencies {

    // = Production

    // - Modules
    implementation(project(":shared"))

    // - Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // = Testing

    // - JUnit 5
    val assertjVersion: String by project.ext
    val jsonUnitVersion: String by project.ext
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:$jsonUnitVersion")

    // - Kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // - Spock
    val groovyVersion: String by project.ext
    implementation(platform("org.apache.groovy:groovy-bom:$groovyVersion"))
    implementation("org.apache.groovy:groovy")

    val spockVersion: String by project.ext
    val byteBuddyVersion: String by project.ext
    val objenesisVersion: String by project.ext
    testImplementation(platform("org.spockframework:spock-bom:$spockVersion"))
    testImplementation("org.spockframework:spock-core")
    testRuntimeOnly("net.bytebuddy:byte-buddy:$byteBuddyVersion") // allows mocking of classes in addition to interfaces
    testRuntimeOnly("org.objenesis:objenesis:$objenesisVersion")  // allows mocking of classes without default constructor (together with ByteBuddy or CGLIB)

    // - SpringBoot
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
    }
    testImplementation("io.projectreactor:reactor-test")

    // - Contract testing
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
    testImplementation("org.springframework.cloud:spring-cloud-contract-spec-kotlin")

    // - Functional testing
    val cucumberVersion: String by project.ext
    add("functionalTestImplementation", project(":shared"))
    add("functionalTestImplementation", "io.cucumber:cucumber-java8:$cucumberVersion")
    add("functionalTestImplementation", "io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    add("functionalTestImplementation", "io.cucumber:cucumber-picocontainer:$cucumberVersion")
    add("functionalTestImplementation", "org.assertj:assertj-core:$assertjVersion")
    add("functionalTestImplementation", "com.jayway.jsonpath:json-path")
    add("functionalTestImplementation", "net.javacrumbs.json-unit:json-unit-assertj:$jsonUnitVersion")

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

    test {
        useJUnitPlatform {
            excludeTags("integration")
        }
    }

    register<Test>("integrationTest") {
        useJUnitPlatform {
            includeTags("integration")
        }
        shouldRunAfter("test")
    }

    contractTest {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", "gradle")
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
        afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
            if (desc.parent == null) {
                if (result.testCount == 0L) {
                    throw IllegalStateException("No tests were found. Failing the build")
                } else {
                    println("Results: (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
                }
            } else { /* Nothing to do here */
            }
        }))
    }

    check {
        if (project.hasProperty("integrationTests")) {
            dependsOn("integrationTest")
        }
    }

    withType<Delete> {
        doFirst {
            delete("~/.m2/repository/clean/the/forest/clean-the-forest-gradle")
        }
    }

}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {

            artifact(tasks.named("bootJar"))
            artifact(tasks.named("verifierStubsJar"))

            // https://github.com/spring-gradle-plugins/dependency-management-plugin/issues/273
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}
