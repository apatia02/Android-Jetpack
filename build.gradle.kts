// Top-level build file where you can add configuration options common to all sub-projects/modules.

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin

plugins {
    val gradleVersion = "7.4.2"
    val kotlinVersion = "1.7.0"
    id("com.android.application") version gradleVersion apply false
    id("com.android.library") version gradleVersion apply false
    id("com.google.dagger.hilt.android") version "2.42" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

subprojects {
    project.plugins.applyBaseConfig(project)
}

fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is AppPlugin
            -> project.extensions.getByType<AppExtension>()
                .apply { baseConfig() }

            is LibraryPlugin
            -> project.extensions.getByType<LibraryExtension>()
                .apply { baseConfig() }
        }
    }
}

fun BaseExtension.baseConfig() {
    compileSdkVersion(libs.versions.compileSdk.get().toInt())

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf(
                *kotlinOptions.freeCompilerArgs.toTypedArray(),
                "-Xjvm-default=all",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }
}