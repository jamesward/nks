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
