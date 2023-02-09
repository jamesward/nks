package com.example.demo

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DemoApplicationTests(@Autowired val cr: CustomerRepository) {

	@Test
	fun customerRepoWorks() = runBlocking {
		cr.save(Customer(null, "Foo"))
		val customers = cr.findAll()
		Assertions.assertEquals(1, customers.count())
		Assertions.assertNotNull(customers.last().id)
	}

}
