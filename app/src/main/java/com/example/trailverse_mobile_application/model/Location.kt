package com.example.trailverse_mobile_application.model

data class Location(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "Nature",
    val imageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = "",
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    val score: Int get() = upvotes - downvotes
}