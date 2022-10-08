import androidx.compose.runtime.Composable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.renderComposable

@Composable
fun CustomerUI(customers: List<Customer>) {
    Text("hello, world")
}

suspend fun main() {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val customers: List<Customer> = client.get("/customers").body()

    renderComposable("root") {
        CustomerUI(customers)
    }
}