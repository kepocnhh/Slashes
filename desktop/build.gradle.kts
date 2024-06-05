repositories {
    google()
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
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
    implementation("com.github.kepocnhh:Logics:0.1.3-SNAPSHOT") // todo common
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1") // todo common coroutines
}
