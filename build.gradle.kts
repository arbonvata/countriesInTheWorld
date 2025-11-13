// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val objectboxVersion = "5.0.1" // For Groovy build scripts
    // val objectboxVersion by extra("5.0.1") // For Kotlin DSL scripts

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle) // Android Gradle Plugin 8.0+
        classpath("io.objectbox:objectbox-gradle-plugin:$objectboxVersion")
    }
}

    plugins {
        alias(libs.plugins.android.application) apply false
        alias(libs.plugins.kotlin.android) apply false
        alias(libs.plugins.kotlin.compose) apply false
        alias(libs.plugins.kotlin.serialization) apply false
        id("org.jlleitschuh.gradle.ktlint") version "14.0.1" apply false


        alias(libs.plugins.ksp) apply false
        alias(libs.plugins.hilt) apply false


    }
