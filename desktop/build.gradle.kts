import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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
        getByName("commonMain") {
            kotlin.srcDirs("src/common/main/kotlin")
        }
        getByName("jvmMain").dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "org.kepocnhh.slashes.AppKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = rootProject.name
        }
    }
}
