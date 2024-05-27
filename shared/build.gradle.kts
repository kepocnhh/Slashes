repositories {
    google()
    mavenCentral()
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Version.compose
}

dependencies {
    implementation(compose.foundation)
}
