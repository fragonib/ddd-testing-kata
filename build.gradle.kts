import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20" apply false
    kotlin("plugin.spring") version "1.7.22" apply false
    id("org.springframework.boot") version "3.0.5" apply false
    id("io.spring.dependency-management") version "1.1.0"
    id("org.springframework.cloud.contract") version "4.0.2" apply false
    id("com.patdouble.cucumber-jvm") version "0.20" apply false
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

    tasks.withType<KotlinCompile> {
        compilerOptions {
            apiVersion.set(KOTLIN_1_8)
            languageVersion.set(KOTLIN_1_8)
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        }
    }

    dependencyManagement {
        imports {
            mavenBom("io.spring.platform:platform-bom:2.0.8.RELEASE")
        }
    }

    tasks.withType<Test> {
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.register<DependencyReportTask>("allDeps") {
        group = "help"
        description = "Prints all dependencies of the project."
    }

//    tasks.register<TaskReportTask>("taskGraph") {
//        println("Tasks")
//        gradle.taskGraph.whenReady { graph: TaskExecutionGraph  ->
//            graph.forEachIndexed { n, task ->
//                println("${n + 1} $task")
//                task.dependsOn.forEachIndexed { m, depObj ->
//                    println("  ${m + 1} $depObj")
//                }
//            }
//        }
//    }

//gradle.taskGraph.whenReady {
//    println ("Tasks")
//    allTasks.forEachIndexed { n, task ->
//        println("${n + 1} $task")
//        task.dependsOn.forEachIndexed { m, depObj ->
//            println("  ${ m + 1 } $depObj")
//        }
//    }
//}

}

