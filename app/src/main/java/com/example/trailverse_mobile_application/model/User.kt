package com.example.trailverse_mobile_application.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val contributions: Int = 0,
    val reputation: Int = 0
)