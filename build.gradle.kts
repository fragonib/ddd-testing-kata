import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*
import org.gradle.api.tasks.testing.logging.TestLogEvent.*


plugins {
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.kotlin.spring).apply(false)
    alias(libs.plugins.spring.boot).apply(false)
    alias(libs.plugins.spring.dependency.management).apply(false)
    alias(libs.plugins.cucumber.jvm).apply(false)
    alias(libs.plugins.pact).apply(false)
    alias(libs.plugins.taskinfo).apply(true)
}

val catalog = libs

subprojects {

    group = "clean.the.forest"
    version = "0.0.1-SNAPSHOT"

    apply {
        plugin("groovy")
        plugin(catalog.plugins.kotlin.jvm.get().pluginId)
        plugin(catalog.plugins.kotlin.spring.get().pluginId)
        plugin(catalog.plugins.spring.dependency.management.get().pluginId)
        plugin(catalog.plugins.taskinfo.get().pluginId)
        plugin("project-report")
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        val implementation by configurations
        catalog.bundles.kotlin.get().forEach { implementation(it) }
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

    // Task info plugin configuration
    taskinfo {
        isClipped = false
        isColor = true
        isShowTaskTypes = true
        isInternal = false
    }

}

configurations {
    all {
        // Globally exclude JUnit4 from classpath in favor of JUnit5
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}
