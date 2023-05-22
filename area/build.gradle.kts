plugins {
    groovy
    kotlin("plugin.spring")          // SpringBoot visibility (open) over Kotlin classes
    id("org.springframework.boot")   // SpringBoot task to manage project
    id("com.patdouble.cucumber-jvm") // Functional tests with Cucumber
    id("au.com.dius.pact")           // Contract tests with Pact
}

ext {
    set("kotlin.version", libs.versions.kotlin.get())
}

dependencies {
    implementation(project(":shared"))
    api(libs.starter.webflux)
    implementation(libs.bundles.jackson)
}

testing {

    suites {

        val applySpockBasedTestingDependencies = { suite: JvmTestSuite ->

            suite.dependencies {

                implementation(project())
                implementation(project(":shared-testing"))

                // Spock
                implementation.bundle(libs.bundles.spock)
                implementation.bundle(libs.bundles.assertions)

                // SpringBoot Test
                implementation(libs.starter.test)
                implementation(libs.reactor.test)

                // REST Collaborators mocking
                implementation(libs.wiremock)

                // Nettys native libs to be loaded only on Apple Silicon
                val isMacOS = System.getProperty("os.name").startsWith("Mac OS X")
                val isArm64 = System.getProperty("os.arch").lowercase() == "aarch64"
                if (isMacOS && isArm64) {
                    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.90.Final:osx-aarch_64")
                }

            }

        }

        // Unit tests suite
        val test by getting(JvmTestSuite::class) {

            useJUnitJupiter()

            applySpockBasedTestingDependencies(this)

            targets {
                all {
                    testTask.configure {
                        description = "Fast-running unit tests."
                        filter { excludeTestsMatching("*IT") }
                    }
                }
            }

        }

        // Integration tests suite
        @Suppress("unused")
        val integrationTest by registering(JvmTestSuite::class) {

            useJUnitJupiter()

            applySpockBasedTestingDependencies(this)

            // In this project ITs doesn't have independent source folder, they´re collocated with unit test and
            // distinguished by the name of the class (ending with IT)
            sources {
                groovy {
                    setSrcDirs(listOf("src/test/groovy"))
                }
                resources {
                    setSrcDirs(listOf("src/test/resources"))
                }
            }

            targets {
                all {
                    testTask.configure {
                        description = "Slow-running integration tests."
                        filter { includeTestsMatching("*IT") }
                        shouldRunAfter(test)
                    }
                }
            }

        }

        // Contract tests suite
        val contractTest by registering(JvmTestSuite::class) {

            applySpockBasedTestingDependencies(this)

            dependencies {
                implementation(project(":shared-testing"))
                implementation.bundle(libs.bundles.pact)
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                        filter { isFailOnNoMatchingTests = false }
                    }
                }
            }
        }

    }

}

cucumber {
    suite("functionalTest")
    maxParallelForks = 1
    featureRoots = listOf(
        "features"
    )
    stepDefinitionRoots = listOf(
        "clean.the.forest.area.functional"
    )
    plugins = listOf(
        "pretty",
        "clean.the.forest.shared.testing.functional.WireMockLifeCycle",
        "clean.the.forest.shared.testing.functional.AppContainerLifeCycle"
    )
    ignoreFailures = false
    dependencies {
        val functionalTestImplementation by configurations
        functionalTestImplementation(project(":shared-testing"))
        functionalTestImplementation(libs.bundles.cucumber)
        functionalTestImplementation(libs.bundles.assertions)
        functionalTestImplementation(libs.bundles.testContainers)
        functionalTestImplementation(libs.wiremock)
    }
}

pact {

    broker {
        pactBrokerUrl = "https://your-broker-url/"

        // To use basic auth
        pactBrokerUsername = "<USERNAME>"
        pactBrokerPassword = "<PASSWORD>"

        // OR to use a bearer token
        pactBrokerToken = "<TOKEN>"

        // Customise the authentication header from the default `Authorization`
    }

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

    check {
        dependsOn(testing.suites.named("integrationTest"))
        dependsOn(testing.suites.named("contractTest"))
    }

    withType<Delete> {
        doFirst {
            delete("~/.m2/repository/clean/the/forest/clean-the-forest-gradle")
        }
    }

}
