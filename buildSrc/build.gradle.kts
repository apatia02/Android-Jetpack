plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin{
    plugins{
        create("di_module"){
            id = "di_module"
            implementationClass = "DiModule"
        }
        create("common_module"){
            id = "common_module"
            implementationClass = "CommonModule"
        }
    }
}