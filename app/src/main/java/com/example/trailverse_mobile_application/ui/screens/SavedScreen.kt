package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
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
fun SavedScreen(onLocationClick: (String) -> Unit) {
    val context = LocalContext.current
    val locationViewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(context)
    )
    val favoriteViewModel: FavoriteViewModel = viewModel()

    val allLocations by locationViewModel.locations.collectAsState()
    val userVotes by locationViewModel.userVotes.collectAsState()
    val savedIds by favoriteViewModel.savedIds.collectAsState()

    val savedLocations = allLocations.filter { savedIds.contains(it.id) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Saved", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        if (savedLocations.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text("No saved places yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(savedLocations, key = { it.id }) { location ->
                    LocationCard(
                        location = location,
                        userVote = userVotes[location.id] ?: 0,
                        isSaved = true,
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