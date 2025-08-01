plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withSourcesJar()
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(project(":annotation"))
    implementation(libs.bundles.kotlinpoet)
    implementation(libs.ksp.api)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            pom {
                name.set("Kotlin Builder Generator - Processor")
                description.set("KSP processor for generating builder pattern classes")
                url.set("https://github.com/your-username/kotlin-builder-generator")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
}