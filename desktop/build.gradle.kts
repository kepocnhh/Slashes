import org.jetbrains.compose.desktop.application.dsl.TargetFormat

repositories {
    google()
    mavenCentral()
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Version.compose
}

//task("testClasses") { dependsOn("jvmTestClasses") }

//kotlin {
//    jvm()
//    sourceSets {
//        getByName("jvmMain") {
//            kotlin.srcDirs("src/main/kotlin")
//            dependencies {
//                implementation(project(":shared"))
//                implementation(compose.desktop.currentOs)
//            }
//        }
//    }
//}

compose.desktop {
    application {
        mainClass = "org.kepocnhh.slashes.AppKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = rootProject.name
            packageVersion = "1.0.0" // todo
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.desktop.currentOs)
}
