package com.example.nativekotlin

expect annotation class Id()
expect annotation class Serializable()

@Serializable
data class Customer(@Id val id: Int?, val name: String)