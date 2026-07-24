package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trailverse_mobile_application.model.Location
import com.example.trailverse_mobile_application.ui.components.LocationCard

private val exploreLocations = listOf(
    Location("1", "Hidden Waterfall Trail", "A secluded waterfall reachable after a 2hr forest hike.", "Nature", upvotes = 42, downvotes = 3),
    Location("2", "Rooftop Sunset Point", "Best sunset view in the city, free entry after 5pm.", "Viewpoint", upvotes = 88, downvotes = 6),
    Location("3", "Old Town Night Market", "Street food, live music, and local crafts every Friday.", "Food", upvotes = 65, downvotes = 10),
    Location("4", "Cliffside Cave", "Coastal cave only visible at low tide.", "Adventure", upvotes = 30, downvotes = 2)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onLocationClick: (String) -> Unit) {
    var showMap by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Explore", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showMap = !showMap }) {
                        Icon(
                            if (showMap) Icons.Default.List else Icons.Default.Map,
                            contentDescription = if (showMap) "Show list" else "Show map"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        if (showMap) {
            MapScreen(onLocationClick = onLocationClick)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(exploreLocations, key = { it.id }) { location ->
                    LocationCard(
                        location = location,
                        userVote = 0,
                        onClick = { onLocationClick(location.id) },
                        onUpvote = {},
                        onDownvote = {},
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                }
            }
        }
    }
}