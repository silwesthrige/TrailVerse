package com.example.trailverse_mobile_application.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ForestGreen = Color(0xFF2E7D32)
val ForestGreenLight = Color(0xFF60AD5E)
val ForestGreenDark = Color(0xFF005005)
val SunsetOrange = Color(0xFFFF7043)
val SunsetOrangeLight = Color(0xFFFFA270)
val SkyBlue = Color(0xFF29B6F6)
val SandBeige = Color(0xFFF5E9DA)
val CharcoalText = Color(0xFF212121)
val SoftGray = Color(0xFF757575)
val CardBackground = Color(0xFFFFFFFF)
val ErrorRed = Color(0xFFE53935)
val UpvoteGreen = Color(0xFF43A047)
val DownvoteRed = Color(0xFFE53935)

val OceanBlue = Color(0xFF0288D1)
val TealAccent = Color(0xFF00BFA5)
val CoralPink = Color(0xFFFF6F91)
val GoldStar = Color(0xFFFFC107)

// Hero gradients used on Login / Register / Profile
val HeroGradient = Brush.linearGradient(
    colors = listOf(ForestGreenDark, ForestGreen, ForestGreenLight)
)

// Per-category gradient used on location card photos
fun categoryBrush(category: String): Brush = when (category) {
    "Nature" -> Brush.linearGradient(listOf(ForestGreenDark, ForestGreenLight))
    "Food" -> Brush.linearGradient(listOf(SunsetOrange, SunsetOrangeLight))
    "Viewpoint" -> Brush.linearGradient(listOf(OceanBlue, SkyBlue))
    "Adventure" -> Brush.linearGradient(listOf(CoralPink, Color(0xFFFF9EB5)))
    else -> Brush.linearGradient(listOf(TealAccent, Color(0xFF64FFDA)))
}