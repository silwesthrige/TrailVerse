package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trailverse_mobile_application.ui.components.LocationCard
import com.example.trailverse_mobile_application.viewmodel.FavoriteViewModel
import com.example.trailverse_mobile_application.viewmodel.LocationViewModel
import com.example.trailverse_mobile_application.viewmodel.LocationViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onLocationClick: (String) -> Unit) {
    val context = LocalContext.current
    val locationViewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(context)
    )
    val favoriteViewModel: FavoriteViewModel = viewModel()

    val locations by locationViewModel.locations.collectAsState()
    val userVotes by locationViewModel.userVotes.collectAsState()
    val savedIds by favoriteViewModel.savedIds.collectAsState()
    val isLoading by locationViewModel.isLoading.collectAsState()
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
        when {
            showMap -> {
                MapScreen(
                    locations = locations,
                    onLocationClick = onLocationClick
                )
            }
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
                    items(locations, key = { it.id }) { location ->
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