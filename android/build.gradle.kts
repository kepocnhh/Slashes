import com.android.build.api.variant.ComponentIdentity
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sp.gx.core.camelCase
import sp.gx.core.create
import sp.gx.core.getByName
import sp.gx.core.kebabCase

//buildscript {
//    repositories {
//        google()
//    }
//    dependencies {
//        classpath("com.android.tools.build:gradle:8.4.1")
//    }
//}

repositories {
    google()
    mavenCentral()
}

plugins {
    kotlin("multiplatform")
//    kotlin("plugin.compose") version Version.compose
    id("com.android.application")
    id("org.jetbrains.compose") version Version.compose
//    id("kotlin-android")
}

kotlin {
    androidTarget()
    sourceSets {
        getByName("androidMain") {
            kotlin.srcDirs("src/main/kotlin")
            dependencies {
//                implementation(project(":shared"))
                implementation(compose.foundation)
                implementation("androidx.appcompat:appcompat:1.6.1")
            }
        }
    }
}

fun ComponentIdentity.getVersion(): String {
    val versionName = android.defaultConfig.versionName ?: error("No version name!")
    check(versionName.isNotBlank())
    val versionCode = android.defaultConfig.versionCode ?: error("No version code!")
    check(versionCode > 0)
    check(name.isNotBlank())
    return when (buildType) {
        "debug" -> kebabCase(
            versionName,
            name,
            versionCode.toString(),
        )
        "release" -> when (flavorName) {
            "watch" -> kebabCase(
                versionName,
                "watch",
                versionCode.toString(),
            )
            "phone" -> kebabCase(
                versionName,
                versionCode.toString(),
            )
            else -> error("Flavor \"${flavorName}\" is not supported!")
        }
        else -> error("Build type \"${buildType}\" is not supported!")
    }
}

android {
    namespace = "org.kepocnhh.slashes"
    compileSdk = Version.Android.compileSdk

    defaultConfig {
        applicationId = namespace
        minSdk = Version.Android.minSdk
        targetSdk = Version.Android.targetSdk
        versionCode = 1
        versionName = "0.0.$versionCode"
        manifestPlaceholders["appName"] = "@string/app_name"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
            isMinifyEnabled = false
            isShrinkResources = false
            manifestPlaceholders["buildType"] = name
        }
    }

    productFlavors {
        "device".also { dimension ->
            flavorDimensions += dimension
            create("phone") {
                this.dimension = dimension
            }
            create("watch") {
                this.dimension = dimension
                applicationIdSuffix = ".$name"
                versionNameSuffix = ".$name"
            }
        }
    }

    sourceSets.getByName("main") {
        manifest.srcFile("src/$name/AndroidManifest.xml")
        res.srcDirs("src/$name/res")
    }

//    composeOptions.kotlinCompilerExtensionVersion = Version.compose
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.5"
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.6"
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.8"
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.10"
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.11"
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.12"
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.13"
//    composeOptions.kotlinCompilerExtensionVersion = "1.5.14"

    buildFeatures {
//        compose = true
        buildConfig = true
    }
}

androidComponents.onVariants { variant ->
    val output = variant.outputs.single()
    check(output is com.android.build.api.variant.impl.VariantOutputImpl)
    output.outputFileName = "${kebabCase(rootProject.name, variant.getVersion())}.apk"
    afterEvaluate {
        tasks.getByName<JavaCompile>("compile", variant.name, "JavaWithJavac") {
            targetCompatibility = Version.jvmTarget
        }
        tasks.getByName<KotlinCompile>("compile", variant.name, "KotlinAndroid") {
            kotlinOptions.jvmTarget = Version.jvmTarget
        }
        val checkManifestTask = tasks.create("checkManifest", variant.name) {
            dependsOn(camelCase("compile", variant.name, "Sources"))
            doLast {
                val file = "intermediates/merged_manifest/${variant.name}/AndroidManifest.xml"
                val manifest = groovy.xml.XmlParser().parse(layout.buildDirectory.file(file).get().asFile)
                val actual = manifest.getAt(groovy.namespace.QName("uses-permission")).map {
                    check(it is groovy.util.Node)
                    val attributes = it.attributes().mapKeys { (k, _) -> k.toString() }
                    val name = attributes["{http://schemas.android.com/apk/res/android}name"]
                    check(name is String && name.isNotEmpty())
                    name
                }
                val applicationId by variant.applicationId
                val expected = setOf(
                    "$applicationId.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION",
                )
                check(actual.sorted() == expected.sorted()) {
                    "Actual is:\n$actual\nbut expected is:\n$expected"
                }
            }
        }
        tasks.getByName(camelCase("assemble", variant.name)) {
            dependsOn(checkManifestTask)
        }
    }
}

/*
dependencies {
    debugImplementation("androidx.compose.ui:ui-tooling:${Version.Android.compose}")
    debugImplementation("androidx.compose.ui:ui-tooling-preview:${Version.Android.compose}")
    debugImplementation("androidx.wear:wear-tooling-preview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.foundation:foundation:${Version.compose}")
    implementation("androidx.security:security-crypto:1.0.0")
    implementation("com.github.kepocnhh:Logics:0.1.3-SNAPSHOT")
    implementation("com.github.kepocnhh:Storages:0.4.2u-SNAPSHOT")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    "watchImplementation"("androidx.wear.compose:compose-foundation:1.3.1")
}
*/