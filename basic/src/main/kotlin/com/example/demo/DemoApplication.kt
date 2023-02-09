package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.web.reactive.function.server.*

@SpringBootApplication
class DemoApplication {

	@Bean
	fun http(cr: CustomerRepository) = coRouter {
		GET("/customers") {
			ServerResponse.ok().bodyAndAwait(cr.findAll())
		}
		POST("/customers") { req ->
			val resp = req.awaitBodyOrNull<Customer>()?.let {
				cr.save(it)
				ServerResponse.noContent()
			} ?: ServerResponse.badRequest()

			resp.buildAndAwait()
		}
	}
}

interface CustomerRepository : CoroutineCrudRepository<Customer, Int>

data class Customer(@Id val id: Int?, val name: String)

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
