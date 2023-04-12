plugins {
	kotlin("multiplatform") version "1.8.20"
}

repositories {
	mavenCentral()
	maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
}

dependencies {

}

kotlin {
	wasm {
		binaries.executable()
		browser {
			commonWebpackConfig {
				devServer = (devServer ?: org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer()).copy(
					proxy = mutableMapOf("/customers" to "http://localhost:8080"),
					open = mapOf(
						"app" to mapOf(
							"name" to "xdg-open"
						)
					)
				)
			}
		}
	}
	sourceSets {
		val wasmMain by getting {
			dependencies {
				implementation(project(":common"))
				implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta-wasm0")
				implementation("org.jetbrains.kotlinx:atomicfu:0.18.5-wasm0")
			}
		}
	}
}
