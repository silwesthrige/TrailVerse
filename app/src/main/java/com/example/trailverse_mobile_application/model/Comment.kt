package com.example.trailverse_mobile_application.model

data class Comment(
    val id: String = "",
    val locationId: String = "",
    val userId: String = "",
    val userName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)