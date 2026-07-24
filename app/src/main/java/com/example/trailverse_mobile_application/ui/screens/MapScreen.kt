package com.example.trailverse_mobile_application.ui.screens


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.trailverse_mobile_application.model.Location
import com.example.trailverse_mobile_application.ui.theme.categoryBrush

private data class MapPin(val location: Location, val x: Float, val y: Float)

private val mapLocations = listOf(
    MapPin(Location("1", "Hidden Waterfall Trail", "A secluded waterfall reachable after a 2hr forest hike.", "Nature", upvotes = 42, downvotes = 3), 0.28f, 0.32f),
    MapPin(Location("2", "Rooftop Sunset Point", "Best sunset view in the city, free entry after 5pm.", "Viewpoint", upvotes = 88, downvotes = 6), 0.62f, 0.22f),
    MapPin(Location("3", "Old Town Night Market", "Street food, live music, and local crafts every Friday.", "Food", upvotes = 65, downvotes = 10), 0.45f, 0.58f),
    MapPin(Location("4", "Cliffside Cave", "Coastal cave only visible at low tide.", "Adventure", upvotes = 30, downvotes = 2), 0.75f, 0.68f)
)

@Composable
fun MapScreen(onLocationClick: (String) -> Unit) {
    var selectedPin by remember { mutableStateOf<MapPin?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Mock map surface
        Canvas(modifier = Modifier.fillMaxSize().background(Color(0xFFE8F0E3))) {
            val roadColor = Color(0xFFD3DED0)
            drawLine(roadColor, Offset(0f, size.height * 0.25f), Offset(size.width, size.height * 0.2f), strokeWidth = 26f, cap = StrokeCap.Round)
            drawLine(roadColor, Offset(0f, size.height * 0.65f), Offset(size.width, size.height * 0.7f), strokeWidth = 22f, cap = StrokeCap.Round)
            drawLine(roadColor, Offset(size.width * 0.3f, 0f), Offset(size.width * 0.35f, size.height), strokeWidth = 18f, cap = StrokeCap.Round)
            drawLine(roadColor, Offset(size.width * 0.7f, 0f), Offset(size.width * 0.65f, size.height), strokeWidth = 20f, cap = StrokeCap.Round)
            drawCircle(Color(0xFFDCE9D5), radius = size.minDimension * 0.22f, center = Offset(size.width * 0.2f, size.height * 0.75f))
            drawCircle(Color(0xFFDCE9D5), radius = size.minDimension * 0.16f, center = Offset(size.width * 0.85f, size.height * 0.3f))
        }

        // Pins
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val w = maxWidth
            val h = maxHeight
            mapLocations.forEach { pin ->
                MapMarker(
                    pin = pin,
                    selected = selectedPin?.location?.id == pin.location.id,
                    modifier = Modifier
                        .offset(x = w * pin.x - 20.dp, y = h * pin.y - 40.dp)
                        .align(Alignment.TopStart)
                ) {
                    selectedPin = pin
                }
            }
        }

        // Top search bar
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                Text("Explore on map", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // My-location floating button
        FloatingActionButton(
            onClick = {},
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "My location", tint = MaterialTheme.colorScheme.primary)
        }

        // Preview card for selected pin
        selectedPin?.let { pin ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .clickable { onLocationClick(pin.location.id) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(categoryBrush(pin.location.category)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            pin.location.name,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            pin.location.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MapMarker(
    pin: MapPin,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(if (selected) 48.dp else 40.dp)
            .clip(CircleShape)
            .background(if (selected) MaterialTheme.colorScheme.primary else Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.LocationOn,
            contentDescription = pin.location.name,
            tint = if (selected) Color.White else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(if (selected) 26.dp else 22.dp)
        )
    }
}