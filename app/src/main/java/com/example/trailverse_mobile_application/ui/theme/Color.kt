package com.example.trailverse_mobile_application.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core palette — pulled from the logo
val SunsetOrange = Color(0xFFFF6B35)
val SunsetOrangeLight = Color(0xFFFF9558)
val SunsetRed = Color(0xFFE63946)
val SunGold = Color(0xFFFFB627)
val SunGoldLight = Color(0xFFFFD166)
val OceanBlue = Color(0xFF1B6CA8)
val OceanBlueDeep = Color(0xFF14477A)
val SkyBlue = Color(0xFF3AA8DB)
val SkyBlueLight = Color(0xFF6FC3E8)

// Neutral / surface tones
val SandBeige = Color(0xFFFFF8F0)
val CharcoalText = Color(0xFF20242C)
val SoftGray = Color(0xFF757575)
val CardBackground = Color(0xFFFFFFFF)
val ErrorRed = Color(0xFFE53935)
val UpvoteGreen = Color(0xFF43A047)
val DownvoteRed = Color(0xFFE53935)
val GoldStar = SunGold

// Splash / hero gradient
val HeroGradient = Brush.verticalGradient(
    colors = listOf(OceanBlueDeep, OceanBlue, SkyBlue)
)

val SunsetGradient = Brush.linearGradient(
    colors = listOf(SunsetRed, SunsetOrange, SunGold)
)

val SplashGradient = Brush.verticalGradient(
    colors = listOf(OceanBlueDeep, OceanBlue, SkyBlueLight, SunGoldLight, SunsetOrange)
)

fun categoryBrush(category: String): Brush = when (category) {
    "Nature" -> Brush.linearGradient(listOf(OceanBlueDeep, OceanBlue))
    "Food" -> Brush.linearGradient(listOf(SunsetRed, SunsetOrange))
    "Viewpoint" -> Brush.linearGradient(listOf(SkyBlue, SkyBlueLight))
    "Adventure" -> Brush.linearGradient(listOf(SunsetOrange, SunGold))
    else -> Brush.linearGradient(listOf(OceanBlue, SkyBlue))
}