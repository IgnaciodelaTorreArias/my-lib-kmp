import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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
//    sourceSets {
//        val commonMain by getting {
//            kotlin.srcDir("build/generated/source/wire")
//        }
//    }
//    jvm {
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_22)
//        }
//    }
    mingwX64()
    applyDefaultHierarchyTemplate()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.wire:wire-runtime:6.2.0")
            }
        }
    }
    targets.withType<KotlinNativeTarget>().configureEach {
        print(targetName)
//        val main by compilations.getting
//        val interop by main.cinterops.creating

        compilations.getByName("main") {
            cinterops {
                val MyRustProtos by creating {
                    definitionFile.set(project.file("src/nativeInterop/cinterop/libmy_rust_protos.def"))
//                    compilerOpts("-I/path")
//                    includeDirs.allHeaders("path")
                }
            }
        }
        binaries {
            executable {
                entryPoint = "io.github.ignaciodelatorrearias.greet.main"
            }
        }
    }
}