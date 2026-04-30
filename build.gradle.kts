import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "2.3.20"
    id("com.squareup.wire") version "6.2.0"
    id("com.vanniktech.maven.publish") version "0.36.0"
}

group = "io.github.ignaciodelatorrearias"
version = "1.0.3"

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
    jvm ()
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
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_22
    }
}
tasks.withType<JavaCompile>().configureEach {
    options.release = 22
}
mavenPublishing {
    publishToMavenCentral()
    coordinates("io.github.ignaciodelatorrearias", "greet", "1.0.2-SNAPSHOT")
    pom {
        name.set("Greet Multiplatform Library")
        description.set("A library for testing FFI with kotlin multiplatform")
        inceptionYear.set("2026")
        url.set("https://github.com/IgnaciodelaTorreArias/my-lib-kmp/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("IgnaciodelaTorreArias")
                name.set("Ignacio de la Torre Arias")
                url.set("https://github.com/IgnaciodelaTorreArias/")
            }
        }
        scm {
            url.set("https://github.com/IgnaciodelaTorreArias/my-lib-kmp/")
            connection.set("scm:git:git://github.com/IgnaciodelaTorreArias/my-lib-kmp.git")
            developerConnection.set("scm:git:ssh://git@github.com/IgnaciodelaTorreArias/my-lib-kmp.git")
        }
    }
    signAllPublications()
}
