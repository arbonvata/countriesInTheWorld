// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle) // Android Gradle Plugin 8.0+
        classpath(libs.objectbox.gradle.plugin)
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
        id("com.android.legacy-kapt") version "9.4.1" apply false


    }
