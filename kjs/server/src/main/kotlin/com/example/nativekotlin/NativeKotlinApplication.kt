package com.example.nativekotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@SpringBootApplication
class NativeKotlinApplication {

    @Bean
    fun http(cr: CustomerRepository) = coRouter {
        GET("/customers") {
            ServerResponse.ok().bodyAndAwait(cr.findAll())
        }
    }

    @Bean
    fun myListener(cr: CustomerRepository) = MyListener(cr)
}


fun main(args: Array<String>) {
    runApplication<NativeKotlinApplication>(*args)
}

class MyListener(val customerRepository: CustomerRepository) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        runBlocking {
            val customers: Flow<Customer> = flowOf("James", "Josh").map { Customer(null, it) }
            customerRepository.saveAll(customers).collect { println (it) }//look ma, no Flow!
        }
    }
}


interface CustomerRepository : CoroutineCrudRepository<Customer, Int>
