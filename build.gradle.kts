import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "2.3.20"
    id("com.gradleup.shadow") version "9.4.1"
    id("com.squareup.wire") version "6.2.0"
}

group = "io.github.ignaciodelatorrearias"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

wire {
    sourcePath {
        srcDir("my_rust_protos/protos")
    }
    kotlin {}
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}
kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_22)
        }
    }
    mingwX64()
    linuxX64()
    linuxArm64()
    macosArm64()
    applyDefaultHierarchyTemplate()
    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.getByName("main") {
            cinterops {
                val MyRustProtos by creating {
                    definitionFile.set(project.file("src/nativeInterop/cinterop/libmy_rust_protos.def"))
                }
            }
        }
        binaries {
            executable {
                entryPoint = "io.github.ignaciodelatorrearias.greet.main"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.wire:wire-runtime:6.2.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.fizzed:jne:4.11.0")
            }
        }
    }
}
tasks.named<Jar>("shadowJar") {
    manifest {
        attributes["Main-Class"] = "io.github.ignaciodelatorrearias.greet.MainKt"
    }
}
