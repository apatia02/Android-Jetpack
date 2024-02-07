plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
}
android {
    apply(file("../commonModule.gradle"))
}
dependencies {
    implementation("com.google.android.material:material:${Versions.materialVersion}")
}