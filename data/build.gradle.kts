plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
}
android {
    apply(file("../commonModule.gradle"))
}

dependencies {
    //noinspection GradleDependency
    implementation(project(":domain"))
    implementation(project(":base_resources"))
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}") // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}") // Gson Converter
    implementation("androidx.room:room-runtime:${Versions.roomVersion}") // Room
    annotationProcessor("androidx.room:room-compiler:${Versions.roomVersion}") // Room annotation processor
}