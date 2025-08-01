plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(project(":annotation"))
    ksp(project(":processor"))
    
    // For testing
    testImplementation(libs.bundles.testing)
}

tasks.test {
    useJUnitPlatform()
}