plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    di_module
    common_module
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.retrofitLib) // Retrofit
    implementation(libs.gsonConverter) // Gson Converter
    implementation(libs.roomRuntime) // Room
    kapt(libs.roomCompiler) // Room annotation processor
}