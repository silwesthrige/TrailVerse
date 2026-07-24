package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trailverse_mobile_application.model.Location
import com.example.trailverse_mobile_application.ui.components.LocationCard
import com.example.trailverse_mobile_application.ui.components.SearchBar

private val sampleLocations = listOf(
    Location("1", "Hidden Waterfall Trail", "A secluded waterfall reachable after a 2hr forest hike.", "Nature", upvotes = 42, downvotes = 3),
    Location("2", "Rooftop Sunset Point", "Best sunset view in the city, free entry after 5pm.", "Viewpoint", upvotes = 88, downvotes = 6),
    Location("3", "Old Town Night Market", "Street food, live music, and local crafts every Friday.", "Food", upvotes = 65, downvotes = 10),
    Location("4", "Cliffside Cave", "Coastal cave only visible at low tide.", "Adventure", upvotes = 30, downvotes = 2)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLocationClick: (String) -> Unit,
    onAddLocation: () -> Unit,
    onProfileClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var votes by remember { mutableStateOf(mapOf<String, Int>()) }

    val filtered = remember(query) {
        sampleLocations.filter { it.name.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("TrailVerse", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddLocation) {
                Icon(Icons.Default.Add, contentDescription = "Add location")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                SearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(filtered, key = { it.id }) { location ->
                LocationCard(
                    location = location,
                    userVote = votes[location.id] ?: 0,
                    onClick = { onLocationClick(location.id) },
                    onUpvote = {
                        votes = votes.toMutableMap().apply { this[location.id] = if (this[location.id] == 1) 0 else 1 }
                    },
                    onDownvote = {
                        votes = votes.toMutableMap().apply { this[location.id] = if (this[location.id] == -1) 0 else -1 }
                    },
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }
        }
    }
}