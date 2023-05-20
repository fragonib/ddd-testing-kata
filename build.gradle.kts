import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*
import org.gradle.api.tasks.testing.logging.TestLogEvent.*


plugins {
    kotlin("jvm") version "1.8.20" apply false
    kotlin("plugin.spring") version "1.7.22" apply false
    id("org.springframework.boot") version "3.0.5" apply false
    id("io.spring.dependency-management") version "1.1.0"
    id("com.patdouble.cucumber-jvm") version "0.20" apply false
    id("au.com.dius.pact") version "4.3.10" apply false
    id("org.barfuin.gradle.taskinfo") version "2.1.0"
}

subprojects {

    group = "clean.the.forest"
    version = "0.0.1-SNAPSHOT"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("io.spring.dependency-management")
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {

        // - Kotlin
        val implementation by configurations
        implementation(platform(kotlin("bom")))
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")

    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<GroovyCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            apiVersion.set(KOTLIN_1_8)
            languageVersion.set(KOTLIN_1_8)
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        }
    }

    tasks.withType<Test> {
        testLogging {
            exceptionFormat = FULL
            showExceptions = true
            showStandardStreams = true
            events(PASSED, FAILED, SKIPPED, STANDARD_ERROR)
        }
    }

    dependencyManagement {
        imports {
            mavenBom("io.spring.platform:platform-bom:2.0.8.RELEASE")
        }
    }

}

configurations {
    all {
        // Globally exclude JUnit4 from classpath in favor of JUnit5
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

// Task info plugin configuration
taskinfo {
    isClipped = false
    isColor = true
    isShowTaskTypes = true
    isInternal = false
}
