plugins {
    groovy
    kotlin("jvm")
}

ext {
    set("kotlin.version", "1.8.20")
    set("testContainersVersion", "1.18.1")
    set("cucumberVersion", "7.11.1")
    set("wiremockVersion", "3.0.0-beta-8")
    set("spockVersion", "2.3-groovy-4.0")
}

dependencies {

    // - Integration testing
    implementation("org.mockito:mockito-core")
    val wiremockVersion: String by project.ext
    implementation("com.github.tomakehurst:wiremock-standalone:${wiremockVersion}")

    // -- Collaborator support
    val spockVersion: String by project.ext
    implementation("org.spockframework:spock-core:$spockVersion")
    val bootVersion: String by project
    implementation("org.springframework.boot:spring-boot:$bootVersion")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-test")

    // - Functional testing
    val cucumberVersion: String by project.ext
    val testContainersVersion: String by project.ext
    implementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    implementation("org.testcontainers:testcontainers:$testContainersVersion")
    implementation("io.quarkus:quarkus-junit4-mock:2.11.2.Final")  // Avoid Testcontainers JUnit4 dependency

}

tasks {
    // Compile Kotlin before Groovy in this project
    compileGroovy {
        dependsOn(compileKotlin)
        classpath += files(compileKotlin)
    }
    classes {
        dependsOn(compileGroovy)
    }
}
