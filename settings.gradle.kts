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
        maven{setUrl("https://jitpack.io")}
    }
}

rootProject.name = "AndroidJetpack"
include(":app")
include(":data")
include(":domain")
include(":presentation")
include(":base_resources")
