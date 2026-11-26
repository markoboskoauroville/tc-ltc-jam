plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val appVersion = (project.findProperty("appVersion") as String).toInt()

android {
    namespace = "com.mantraproductions.tcltcjam"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mantraproductions.tcltcjam"
        minSdk = 29
        targetSdk = 35
        versionCode = appVersion
        versionName = appVersion.toString()
        ndk { abiFilters += "arm64-v8a" }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
