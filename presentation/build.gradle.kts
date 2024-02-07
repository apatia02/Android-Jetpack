plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
}
android {
    apply(file("../commonModule.gradle"))
    buildFeatures {
        viewBinding = true
    }
}
dependencies{
    implementation(project(":domain"))
    implementation(project(":base_resources"))
}