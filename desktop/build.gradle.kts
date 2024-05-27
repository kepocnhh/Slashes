repositories {
    google()
    mavenCentral()
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Version.compose
}

compose.desktop {
    application {
        mainClass = "org.kepocnhh.slashes.AppKt" // todo
        nativeDistributions.packageName = rootProject.name
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.desktop.currentOs)
}
