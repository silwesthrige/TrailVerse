package com.example.trailverse_mobile_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.trailverse_mobile_application.navigation.TrailVerseNavGraph
import com.example.trailverse_mobile_application.ui.theme.TrailVerse_Mobile_ApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrailVerse_Mobile_ApplicationTheme {
                TrailVerseNavGraph()
            }
        }
    }
}