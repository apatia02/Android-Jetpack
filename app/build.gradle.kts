plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    di_module
    common_module
}

android {
    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    implementation(project(":base_resources"))
    implementation(libs.retrofitLib)
    implementation(libs.gsonConverter)
}