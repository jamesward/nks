package com.example.nativekotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.data.annotation.Id
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter
import java.time.Instant
import java.time.ZonedDateTime

// https://github.com/oracle/graalvm-reachability-metadata/
@ImportRuntimeHints(MyHints::class)
@SpringBootApplication
class DemoApplication {

    @Bean
    fun runner(cr: CustomerRepository) = ApplicationRunner {
        runBlocking {
            val customers: Flow<Customer> = flowOf("James", "Josh").map { Customer(null, it) }
            cr.saveAll(customers).collect { println(it) }//look ma, no Flow!
        }
    }

    @Bean
    fun http(cr: CustomerRepository) = coRouter {
        GET("/customers") {
            ServerResponse.ok().bodyAndAwait(cr.findAll())
        }
    }
}

class MyHints : RuntimeHintsRegistrar {

    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        listOf(Customer::class.java, Array<Instant>::class.java, Array<ZonedDateTime>::class.java).forEach {
            hints.reflection().registerType(it, *MemberCategory.values())
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

interface CustomerRepository : CoroutineCrudRepository<Customer, Int>

data class Customer(@Id val id: Int? = null, val name: String? = null)