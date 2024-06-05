repositories {
//    google()
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

plugins {
    kotlin("jvm")
//    id("org.jetbrains.compose") version Version.compose
}

dependencies {
//    implementation(compose.foundation)
    implementation("com.github.kepocnhh:Logics:0.1.3-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}
