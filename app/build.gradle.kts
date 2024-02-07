plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = Versions.applicationId
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    implementation(project(":base_resources"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}")
    implementation("com.github.bumptech.glide:glide:${Versions.glideVersion}") // Glide
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}") // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}") // Gson Converter
    implementation("androidx.room:room-runtime:${Versions.roomVersion}") // Room
    annotationProcessor("androidx.room:room-compiler:${Versions.roomVersion}") // Room annotation processor
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}") // Coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}") // ViewModel
    implementation("com.google.dagger:hilt-android:${Versions.hiltVersion}") // Hilt
    annotationProcessor("com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}") // Hilt annotation processor
    implementation("androidx.paging:paging-runtime-ktx:${Versions.pagingVersion}") // Jetpack Paging
    implementation("com.google.android.material:material:${Versions.materialVersion}")
    implementation("androidx.core:core-ktx:${Versions.coreKtxVersion}")
    implementation("androidx.appcompat:appcompat:${Versions.appCompatVersion}")
    testImplementation("junit:junit:${Versions.junitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.junitExtVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espressoVersion}")
}