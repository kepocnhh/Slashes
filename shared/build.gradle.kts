repositories {
    google()
    mavenCentral()
}

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version Version.compose
}

task("testClasses") { dependsOn("jvmTestClasses") }

kotlin {
    jvm()
    sourceSets {
        getByName("jvmMain") {
            kotlin.srcDirs("src/main/kotlin")
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
