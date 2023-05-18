import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

group = "com.pradeep.moodtracker"
version = "1.0-SNAPSHOT"
val ktorVersion = extra["ktor.version"]

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    kotlin("native.cocoapods")
}

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(11)
    }
    js(IR) {
        browser()
    }

    wasm {
        browser()
    }

    ios()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Common Module"
        homepage = "https://github.com/JetBrains/compose-jb"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "common"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }


        val jsWasmMain by creating {
            dependsOn(commonMain)
        }

        val jsMain by getting {
            dependsOn(jsWasmMain)
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }

        val wasmMain by getting {
            dependsOn(jsWasmMain)
        }

        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.6.1")
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}

android {
    compileSdkVersion(33)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(33)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Use a proper version of webpack, TODO remove after updating to Kotlin 1.9.
rootProject.the<NodeJsRootExtension>().versions.webpack.version = "5.76.2"
