package com.example.trailverse_mobile_application.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ADD_LOCATION = "add_location"
    const val DETAIL = "detail/{locationId}"
    const val PROFILE = "profile"

    fun detail(locationId: String) = "detail/$locationId"
}