plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {

    }

    wasm {
        browser()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("org.springframework.data:spring-data-commons:2.7.2")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.javaParameters = true // this is needed for Spring to reflect parameter names
}
