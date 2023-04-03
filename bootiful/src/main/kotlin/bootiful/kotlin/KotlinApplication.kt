package bootiful.kotlin


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun main(args: Array<String>) {
    runApplication<KotlinApplication>(*args)
}

@SpringBootApplication
class KotlinApplication {

    @Bean
    fun runner(cr: CustomerRepository) = ApplicationRunner {
        runBlocking {
            val customers: Flow<Customer> = flowOf("James", "Josh")
                .map { Customer(null, it) }
            cr.saveAll(customers).collect { println(it) }//look ma, no Flow!
        }
    }

    @Bean
    fun http(cr: CustomerRepository) = coRouter {
        GET("/customers/{id}") {
            val id = it.pathVariable("id").toInt()
            cr
                .findById(id)?.let { customer ->
                    ServerResponse.ok().bodyValueAndAwait(customer)
                } ?: ServerResponse.notFound().build().awaitSingle()

        }
    }
}

interface CustomerRepository : CoroutineCrudRepository<Customer, Int>

data class Customer(@Id val id: Int? = null, val name: String? = null)