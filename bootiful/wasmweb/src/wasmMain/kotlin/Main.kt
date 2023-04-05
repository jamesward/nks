import kotlinx.browser.document
import kotlinx.dom.appendText
import kotlinx.browser.window
import kotlin.js.Promise
import kotlinx.coroutines.await

/*
external class PromiseString {
    fun then(f: (String) -> Unit): PromiseString
}

external class Customer(id: Int?, name: String)

external class PromiseJson {
    fun then(f: (Array<Customer>) -> Unit): PromiseJson
}

external class Response {
    val status: Int
    fun text(): PromiseString
    fun json(): PromiseJson
}

external class PromiseResponse {
    fun then(f: (Response) -> Unit): PromiseResponse
}

external fun fetch(url: String): PromiseResponse
 */

//data class Customer(val id: Int?, val name: String)

suspend fun main() {
    val resp = Promise.resolve(window.fetch("/customers".asDynamic()).asDynamic()).await<org.w3c.fetch.Response>()
    val json = Promise.resolve(resp.json().asDynamic()).await<Dynamic>()
    println(json)
    /*
        val resp = it?.unsafeCast<org.w3c.fetch.Response>()
        resp?.json()?.then { json ->
            println(json)
            //println(j.size())
            Unit.asDynamic()
        }
        Unit.asDynamic()
    }
     */
    /*

        println("then")
        println(it).asDynamic()
        /*
        it.json().then { s ->
            println(s)
        }
         */
    }
     */
    //document.body?.appendText("Hello, ${greet()}!")
}
