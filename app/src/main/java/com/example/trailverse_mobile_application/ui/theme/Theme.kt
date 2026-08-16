package com.example.trailverse_mobile_application.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = OceanBlue,
    onPrimary = Color.White,
    primaryContainer = SkyBlueLight,
    onPrimaryContainer = OceanBlueDeep,
    secondary = SunsetOrange,
    onSecondary = Color.White,
    secondaryContainer = SunGoldLight,
    onSecondaryContainer = SunsetRed,
    tertiary = SunGold,
    background = SandBeige,
    surface = CardBackground,
    onBackground = CharcoalText,
    onSurface = CharcoalText,
    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = SkyBlueLight,
    onPrimary = Color.Black,
    primaryContainer = OceanBlueDeep,
    onPrimaryContainer = SkyBlueLight,
    secondary = SunsetOrangeLight,
    onSecondary = Color.Black,
    tertiary = SunGold,
    background = Color(0xFF12161C),
    surface = Color(0xFF1B212B),
    onBackground = Color.White,
    onSurface = Color.White,
    error = ErrorRed
)

@Composable
fun TrailVerse_Mobile_ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}