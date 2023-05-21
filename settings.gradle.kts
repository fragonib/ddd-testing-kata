pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.springframework.boot") {
                val bootVersion: String by settings
                useModule("org.springframework.boot:spring-boot-gradle-plugin:$bootVersion")
            }
        }
    }
}

rootProject.name = "clean-the-forest"

include("app")
include("area")
include("shared")
include("shared-testing")
