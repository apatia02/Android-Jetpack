import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonModule: Plugin<Project> {
    override fun apply(target: Project) {

        target.dependencies.apply {
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
            add("implementation", "androidx.core:core-ktx:1.7.0")
            add("testImplementation", "junit:junit:4.13.2")
            add("androidTestImplementation", "androidx.test.ext:junit:1.1.5")
            add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.5.1")
        }
    }
}