package com.example.trailverse_mobile_application.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val SAVED = "saved"
    const val PROFILE = "profile"
    const val ADD_LOCATION = "add_location"
    const val DETAIL = "detail/{locationId}"

    fun detail(locationId: String) = "detail/$locationId"
}