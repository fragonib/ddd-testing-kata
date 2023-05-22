import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    groovy
    kotlin("jvm")
}

ext {
    set("kotlin.version", libs.versions.kotlin.get())
}

dependencies {

    implementation(platform(SpringBootPlugin.BOM_COORDINATES))

    // - Integration testing
    implementation(libs.wiremock)
    implementation(libs.mockito.core)

    // -- Collaborator support
    implementation(libs.bundles.spockBundle)
    implementation(libs.spring.boot)
    implementation(libs.spring.context)
    implementation(libs.spring.test)

    // - Functional testing
    implementation(libs.cucumber)
    implementation(libs.bundles.testContainersBundle)

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
