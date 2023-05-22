import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("test-report-aggregation")
    id("jacoco-report-aggregation")
}

ext {
    set("kotlin.version", libs.versions.kotlin.get())
}

dependencies {

    implementation(platform(SpringBootPlugin.BOM_COORDINATES))

    // - Modules
    runtimeOnly(project(":area"))

    // - Spring
    implementation(libs.starter.webflux)
    implementation(libs.bundles.jacksonBundle)
    runtimeOnly(libs.starter.actuator)
    runtimeOnly(libs.springdoc.openapi)

}

tasks {

    bootJar {
        enabled = true
    }

    bootBuildImage {
        imageName.set("${rootProject.name}/${project.name}")
    }

    check {
        dependsOn(named<TestReport>("testAggregateTestReport"))
        dependsOn(named<JacocoReport>("testCodeCoverageReport"))
    }

}
