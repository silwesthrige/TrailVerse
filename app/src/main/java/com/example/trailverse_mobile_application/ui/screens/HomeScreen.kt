package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trailverse_mobile_application.ui.components.LocationCard
import com.example.trailverse_mobile_application.ui.components.SearchBar
import com.example.trailverse_mobile_application.viewmodel.FavoriteViewModel
import com.example.trailverse_mobile_application.viewmodel.LocationViewModel
import com.example.trailverse_mobile_application.viewmodel.LocationViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLocationClick: (String) -> Unit,
    onAddLocation: () -> Unit,
    onProfileClick: () -> Unit
) {
    val context = LocalContext.current
    val locationViewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(context)
    )
    val favoriteViewModel: FavoriteViewModel = viewModel()

    var query by remember { mutableStateOf("") }
    val locations by locationViewModel.locations.collectAsState()
    val userVotes by locationViewModel.userVotes.collectAsState()
    val savedIds by favoriteViewModel.savedIds.collectAsState()
    val isLoading by locationViewModel.isLoading.collectAsState()

    val filtered = remember(query, locations) {
        locations
            .filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.city.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
            .sortedByDescending { it.score }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("TrailVerse", fontWeight = FontWeight.Bold) },
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
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            locations.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No locations yet — be the first to add one!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        SearchBar(
                            query = query,
                            onQueryChange = { query = it },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    if (filtered.isEmpty()) {
                        item {
                            Text(
                                "No results for \"$query\"",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                        }
                    } else {
                        items(filtered, key = { it.id }) { location ->
                            LocationCard(
                                location = location,
                                userVote = userVotes[location.id] ?: 0,
                                isSaved = savedIds.contains(location.id),
                                onClick = { onLocationClick(location.id) },
                                onUpvote = { locationViewModel.vote(location.id, 1) },
                                onDownvote = { locationViewModel.vote(location.id, -1) },
                                onToggleSave = { favoriteViewModel.toggleSave(location.id) },
                                modifier = Modifier.padding(bottom = 14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}