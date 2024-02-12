plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    di_module
    common_module
}
android {
    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":base_resources"))
    implementation(libs.glideLib)
    implementation(libs.lifecycleViewModel)
    implementation(libs.pagingRuntime)
    implementation(libs.materialLib)
    implementation(libs.appCompatLib)
    implementation(libs.fragment)
    implementation (libs.easyadapter)
    implementation(libs.swipeRefresh)
}