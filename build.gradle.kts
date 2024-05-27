import sp.gx.core.buildDir
import sp.gx.core.buildSrc

buildscript {
    repositories.mavenCentral()

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        gradle.startParameter.taskRequests.firstOrNull()?.also { ter ->
            val arg = ter.args.firstOrNull()
            when {
                arg == null || "^:?android:\\w+".toRegex().matches(arg) -> {
                    repositories.google()
                    classpath("com.android.tools.build:gradle:8.2.2")
                }
            }
        }
    }
}

task<Delete>("clean") {
    delete = setOf(buildDir(), buildSrc.buildDir())
}
