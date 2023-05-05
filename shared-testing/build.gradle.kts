ext {
    set("kotlin.version", "1.8.20")
    set("testContainersVersion", "1.17.6")
    set("cucumberVersion", "7.11.1")
    set("wiremockVersion", "2.27.2")
}

dependencies {

    // - Integration testing
    implementation("org.mockito:mockito-core")

    // - Functional testing
    val cucumberVersion: String by project.ext
    val testContainersVersion: String by project.ext
    val wiremockVersion: String by project.ext
    implementation("io.cucumber:cucumber-junit-platform-engine:${cucumberVersion}")
    api("org.testcontainers:testcontainers:${testContainersVersion}")
    implementation("io.quarkus:quarkus-junit4-mock:2.11.2.Final")  // Avoid Testcontainers JUnit4 dependency
    api("com.github.tomakehurst:wiremock:${wiremockVersion}")

}
