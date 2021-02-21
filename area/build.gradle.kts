import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5
import org.springframework.cloud.contract.verifier.config.TestMode.EXPLICIT

plugins {
    groovy
    kotlin("jvm")
    kotlin("plugin.spring")     // SpringBoot visibility over Kotlin classes
    id("org.springframework.boot")  // SpringBoot task to manage project
    id("io.spring.dependency-management") // Dependency management (from SpringBoot crew)
    id("org.springframework.cloud.contract") // Contract verifier tasks
    id ("com.patdouble.cucumber-jvm").version("0.19") // Functional tests with Cucumber
    id("maven-publish")
}

group = "clean.the.forest"
version = "0.0.1-SNAPSHOT"

contracts {
    testFramework.set(JUNIT5)
    testMode.set(EXPLICIT)
    packageWithBaseClasses.set("clean.the.forest.area.contract")
}

cucumber {
    suite("functionalTest")
    maxParallelForks = 1
    stepDefinitionRoots = listOf("clean.the.forest.area.functional")
}

dependencyManagement {
    val BOM_VERSION: String by project
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$BOM_VERSION")
    }
}

dependencies {

    // = Dependencies

    // - Modules
    implementation(project(":shared"))

    // - Kotlin
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // - Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // = Testing

    // - JUnit 5
    val assertjVersion = "3.11.1"
    val jsonUnitVersion = "2.24.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:$jsonUnitVersion")

    // - Kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // - Spock
    val groovyVersion = "3.0.7"
    val spockVersion = "2.0-M4-groovy-3.0"
    testImplementation("org.codehaus.groovy:groovy:$groovyVersion")
    testImplementation(platform("org.spockframework:spock-bom:$spockVersion"))
    testImplementation("org.spockframework:spock-core")

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
    val cucumberVersion = "6.10.0"
    add("functionalTestImplementation", project(":shared"))
    add("functionalTestImplementation", "io.cucumber:cucumber-java8:${cucumberVersion}")
    add("functionalTestImplementation", "io.cucumber:cucumber-junit-platform-engine:${cucumberVersion}")
    add("functionalTestImplementation", "io.cucumber:cucumber-picocontainer:${cucumberVersion}")
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
            excludeTags ("integration")
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
                }
                else {
                    println("Results: (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
                }
            } else { /* Nothing to do here */ }
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