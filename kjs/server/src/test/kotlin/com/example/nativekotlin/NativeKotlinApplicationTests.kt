package com.example.nativekotlin.com.example.nativekotlin

import com.example.nativekotlin.CustomerRepository
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NativeKotlinApplicationTests(@Autowired val cr: CustomerRepository) {

	@Test
	fun contextLoads() = runBlocking {
		cr.save(Customer(null, "Foo"))
		val customers = cr.findAll()
		Assertions.assertEquals(3, customers.count())
		Assertions.assertNotNull(customers.last().id)
	}

}
