plugins {
    kotlin("jvm")
}

group = "clean.the.forest"
version = "0.0.1-SNAPSHOT"

ext {
    set("testContainersVersion", "1.15.2")
    set("cucumberVersion", "6.10.0")
    set("wiremockVersion", "2.27.2")
}

dependencyManagement {
    imports {
        mavenBom("io.spring.platform:platform-bom:1.0.1.RELEASE")
    }
}

dependencies {

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // - Integration testing
    implementation("org.mockito:mockito-core")

    // - Functional testing
    val cucumberVersion: String by project.ext
    val testContainersVersion: String by project.ext
    val wiremockVersion: String by project.ext
    implementation("io.cucumber:cucumber-junit-platform-engine:${cucumberVersion}")
    api("org.testcontainers:testcontainers:${testContainersVersion}")
    api("com.github.tomakehurst:wiremock:${wiremockVersion}")

}

