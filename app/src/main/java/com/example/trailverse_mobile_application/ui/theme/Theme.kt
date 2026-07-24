package com.example.trailverse_mobile_application.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    secondary = SunsetOrange,
    onSecondary = Color.White,
    tertiary = SkyBlue,
    background = SandBeige,
    surface = CardBackground,
    onBackground = CharcoalText,
    onSurface = CharcoalText,
    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = ForestGreenLight,
    onPrimary = Color.Black,
    secondary = SunsetOrangeLight,
    onSecondary = Color.Black,
    tertiary = SkyBlue,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
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