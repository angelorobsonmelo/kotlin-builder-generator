plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    `maven-publish`
}

allprojects {
    group = "com.angelorobson.buildergen"
    version = "1.0.0"

    repositories {
        mavenCentral()
        google()
    }
}

// Configure compiler options for all subprojects
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }
}