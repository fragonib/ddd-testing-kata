plugins {
    kotlin("jvm")
    kotlin("plugin.spring")     // SpringBoot visibility over Kotlin classes
    id("org.springframework.boot")  // SpringBoot task to manage project
    id("io.spring.dependency-management") // Dependency management (from SpringBoot crew)
    groovy
}

group = "clean.the.forest"
version = "0.0.1-SNAPSHOT"

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

    // - Spring
    val openApiVersion = "1.5.4"
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    runtimeOnly("org.springdoc:springdoc-openapi-webflux-ui:$openApiVersion")

    // = Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // - JUnit 5
    val junit5Version = "5.1.0"
    val assertjVersion = "3.11.1"
    val jsonUnitVersion = "2.24.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5Version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit5Version")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:$jsonUnitVersion")

    // - Spock
    val groovyVersion = "3.0.7"
    val spockVersion = "2.0-M4-groovy-3.0"
    testImplementation("org.codehaus.groovy:groovy:$groovyVersion")
    testImplementation(platform("org.spockframework:spock-bom:$spockVersion"))
    testImplementation("org.spockframework:spock-core")

    // - SpringBoot
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    // - Functional testing
    val cucumberVersion = "6.10.0"
    testImplementation("io.cucumber:cucumber-java8:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-picocontainer:${cucumberVersion}")

}

tasks {

    bootJar {
        enabled = false
    }

    jar {
        enabled = true
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

    register<Test>("functionalTest") {
        ignoreFailures = true
        systemProperties(project.gradle.startParameter.systemPropertiesArgs)
        systemProperty("cucumber.execution.parallel.enabled", System.getProperty("test.parallel", "false"))
        systemProperty("cucumber.plugin", "json:build/reports/cucumber.json")
        systemProperty("cucumber.publish.quiet", "true")
        useJUnitPlatform {
            excludeTags("disabled")
        }
    }

    check {
        if (project.hasProperty("integrationTests")) {
            dependsOn("integrationTest")
        }
    }

}