allprojects {
    tasks.register("multify") {
    file("settings.gradle.kts").writeText("""
		rootProject.name = "nks"

		include("common", "server", "wasmweb")
	""".trimIndent())

	file("build.gradle.kts").writeText("""
		plugins {
			id("org.springframework.boot") version "3.0.5" apply false
			id("io.spring.dependency-management") version "1.1.0" apply false
			id("org.graalvm.buildtools.native") version "0.9.20" apply false
			kotlin("jvm") version "1.7.22" apply false
			kotlin("plugin.spring") version "1.7.22" apply false
			kotlin("multiplatform") version "1.8.20" apply false
		}
	""".trimIndent())

	file("common/src/commonMain/kotlin").mkdirs()

	file("common/build.gradle.kts").writeText("""
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
	""".trimIndent())

	file("server").mkdirs()
	java.nio.file.Files.move(file("src").toPath(), file("server/src").toPath())

	file("server/build.gradle.kts").writeText("""
		import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

		plugins {
			id("org.springframework.boot") version "3.0.5"
			id("io.spring.dependency-management") version "1.1.0"
			id("org.graalvm.buildtools.native") version "0.9.20"
			kotlin("jvm") version "1.7.22"
			kotlin("plugin.spring") version "1.7.22"
		}

		group = "bootiful"
		version = "0.0.1-SNAPSHOT"
		java.sourceCompatibility = JavaVersion.VERSION_17

		repositories {
			mavenCentral()
		}

		dependencies {
			implementation(project(":common"))
			implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
			implementation("org.springframework.boot:spring-boot-starter-webflux")
			implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
			implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
			implementation("org.jetbrains.kotlin:kotlin-reflect")
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
			runtimeOnly("org.postgresql:postgresql")
			runtimeOnly("org.postgresql:r2dbc-postgresql")
			testImplementation("org.springframework.boot:spring-boot-starter-test")
			testImplementation("io.projectreactor:reactor-test")
			testImplementation("org.testcontainers:junit-jupiter")
			testImplementation("org.testcontainers:postgresql")
			testImplementation("org.testcontainers:r2dbc")
		}

		dependencyManagement {
			imports {
				mavenBom("org.testcontainers:testcontainers-bom:1.17.6")
			}
		}

		tasks.withType<KotlinCompile> {
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict")
				jvmTarget = "17"
			}
		}

		tasks.withType<Test> {
			useJUnitPlatform()
		}
	""".trimIndent())

    
	file("wasmweb/src/wasmMain/kotlin").mkdirs()
    file("wasmweb/src/wasmMain/kotlin/Main.kt").writeText("""
		import bootiful.kotlin.Customer
		import kotlinx.browser.document
		import kotlinx.browser.window
		import kotlinx.coroutines.await
		import kotlinx.dom.appendElement
		import org.w3c.fetch.Response
		import kotlin.js.Promise

		external interface JsCustomerArray {
		    fun at(indice: Int): Customer
		    val length: Int
		}

		suspend fun main() {
		    val resp = Promise.resolve(window.fetch("/customers".asDynamic()).asDynamic()).await<Response>()
		    val json = Promise.resolve(resp.json().asDynamic()).await<JsCustomerArray>()
		    for (i in 0 until json.length) {
		        document.body?.appendElement("li") {
		            textContent = json.at(i).name
		        }
		    }
		}
	""".trimIndent())

	file("wasmweb/src/wasmMain/resources").mkdirs()
	file("wasmweb/src/wasmMain/resources/index.html").writeText("""
		<!DOCTYPE html>
		<html lang="en">
		<head>
		    <meta charset="UTF-8">
		    <title>Kotlin/Wasm Example</title>
		</head>
		<body>
		<script src="wasmweb.js"></script>
		</body>
		</html>
	""".trimIndent())

	file("wasmweb/build.gradle.kts").writeText("""
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
	""".trimIndent())
    }
}
