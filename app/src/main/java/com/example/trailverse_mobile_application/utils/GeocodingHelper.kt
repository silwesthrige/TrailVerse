package com.example.trailverse_mobile_application.utils

import android.content.Context
import android.location.Geocoder
import java.util.Locale

object GeocodingHelper {
    fun getCityName(context: Context, latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses?.firstOrNull()
            address?.locality
                ?: address?.subAdminArea
                ?: address?.adminArea
                ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}