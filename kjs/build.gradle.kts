plugins {
    id("org.jetbrains.compose")                version "1.2.0-beta01" apply false
    id("org.springframework.boot")             version "3.0.0-M5" apply false
    id("io.spring.dependency-management")      version "1.0.14.RELEASE" apply false
    kotlin("jvm")                              version "1.7.10" apply false
    kotlin("js")                               version "1.7.10" apply false
    kotlin("multiplatform")                    version "1.7.10" apply false
    kotlin("plugin.serialization")             version "1.7.10" apply false
    kotlin("plugin.spring")                    version "1.7.10" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://repo.spring.io/milestone")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
