import kotlinx.serialization.Serializable

expect annotation class Id()

@Serializable
data class Customer(@Id val id: Int?, val name: String)