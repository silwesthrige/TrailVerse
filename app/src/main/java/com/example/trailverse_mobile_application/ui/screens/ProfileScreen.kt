package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trailverse_mobile_application.ui.theme.GoldStar
import com.example.trailverse_mobile_application.ui.theme.HeroGradient
import com.example.trailverse_mobile_application.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val stats by profileViewModel.stats.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    var showLogoutConfirm by remember { mutableStateOf(false) }

    val user = profileViewModel.currentUser
    val displayName = user?.displayName?.takeIf { it.isNotBlank() } ?: "Traveler"
    val email = user?.email ?: ""

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Log out?") },
            text = { Text("You'll need to sign in again to add locations, vote, or comment.") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                    profileViewModel.logout()
                    onLoggedOut()
                }) {
                    Text("Log out", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(HeroGradient)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                IconButton(
                    onClick = { showLogoutConfirm = true },
                    modifier = Modifier.padding(8.dp).align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = "Log out", tint = Color.White)
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 44.dp)
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(56.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(displayName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                if (email.isNotBlank()) {
                    Text(
                        email,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(24.dp))
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 20.dp))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard(icon = Icons.Default.Favorite, label = "Contributions", value = "${stats.contributions}")
                        StatCard(icon = Icons.Default.Star, label = "Reputation", value = "${stats.reputation}")
                    }
                }

                Spacer(Modifier.height(28.dp))
                Text(
                    "Badges",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(earnedBadges(stats.contributions, stats.reputation)) { badge ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldStar,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(badge, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

private fun earnedBadges(contributions: Int, reputation: Int): List<String> {
    val badges = mutableListOf("Explorer")
    if (contributions >= 1) badges.add("First Contribution")
    if (contributions >= 5) badges.add("Top Contributor")
    if (reputation >= 10) badges.add("Trusted Local Guide")
    if (contributions == 0 && reputation == 0) badges.add("Just Getting Started")
    return badges
}

@Composable
private fun StatCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.padding(8.dp).width(130.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}