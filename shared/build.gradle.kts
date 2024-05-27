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
//                implementation(compose.foundation)
//            }
//        }
//    }
//}

dependencies {
    implementation(compose.foundation)
}
