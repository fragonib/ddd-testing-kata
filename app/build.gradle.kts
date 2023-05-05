plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

ext {
    set("kotlin.version", "1.8.20")
    set("openApiVersion", "1.5.4")
}

dependencies {

    // = Dependencies

    // - Modules
    runtimeOnly(project(":area"))

    // - Spring
    val openApiVersion: String by project.extra
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    runtimeOnly("org.springdoc:springdoc-openapi-webflux-ui:$openApiVersion")

}

tasks {

    bootJar {
        enabled = true
    }

    bootBuildImage {
        imageName.set("clean-the-forest/${rootProject.name}-${project.name}")
    }

}
