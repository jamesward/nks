plugins {
    kotlin("js")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser {
            runTask {
                // todo: testcontainer server
                devServer = devServer?.copy(port = 8081, proxy = mutableMapOf("/customers" to "http://localhost:8080"))
            }
        }
        binaries.executable()
    }
}

dependencies {
    api(project(":common"))

    //implementation("io.ktor:ktor-client-core:2.1.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.1")
    implementation("io.ktor:ktor-client-js:2.1.1")
    implementation(compose.web.core)
    implementation(compose.runtime)
}
