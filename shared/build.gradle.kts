plugins {
    kotlin("jvm")
}

group = "clean.the.forest"
version = "0.0.1-SNAPSHOT"

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

    implementation("org.mockito:mockito-core")

}

