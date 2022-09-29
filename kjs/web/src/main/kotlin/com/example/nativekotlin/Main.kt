package com.example.nativekotlin

import androidx.compose.runtime.Composable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable


@Composable
fun Customers(l: List<Customer>) {
    Div {
        Text("Customers:")
        Ul {
            l.map {
                Li {
                    Text(value = it.name)
                }
            }
        }
    }
}

suspend fun main() {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val customers = client.get("/customers").body<List<Customer>>()

    renderComposable(rootElementId = "root") {
        Customers(customers)
    }
}
