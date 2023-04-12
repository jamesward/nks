package bootiful.kotlin

import org.springframework.data.annotation.Id

actual data class Customer(@Id val id: Int? = null, actual val name: String)
