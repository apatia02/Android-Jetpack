pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://artifactory.surfstudio.ru/artifactory/libs-release-local") }
    }
}

rootProject.name = "AndroidJetpack"
include(":app")
include(":data")
include(":domain")
include(":presentation")
include(":base_resources")
