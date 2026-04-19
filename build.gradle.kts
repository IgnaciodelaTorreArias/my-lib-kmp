import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
plugins {
    kotlin("multiplatform") version "2.3.20"
    id("com.squareup.wire") version "6.2.0"
}

group = "io.github.ignaciodelatorrearias"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

wire {
    sourcePath {
        srcDir("my_rust_protos/protos")
    }
    kotlin {}
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_22)
        }
    }
    applyDefaultHierarchyTemplate()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.wire:wire-runtime:6.2.0")
            }
        }
    }
}