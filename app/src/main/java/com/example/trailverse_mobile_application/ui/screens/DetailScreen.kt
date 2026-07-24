package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trailverse_mobile_application.model.Comment
import com.example.trailverse_mobile_application.ui.components.VoteButton
import com.example.trailverse_mobile_application.ui.theme.GoldStar
import com.example.trailverse_mobile_application.ui.theme.categoryBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(locationId: String, onBack: () -> Unit) {
    var userVote by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(42) }
    var commentText by remember { mutableStateOf("") }
    val comments = remember {
        mutableStateOf(
            listOf(
                Comment("c1", locationId, "u1", "Alex", "Absolutely stunning, go early to avoid crowds!"),
                Comment("c2", locationId, "u2", "Priya", "The trail is steep but worth it.")
            )
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Hero header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(categoryBrush("Nature"))
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.align(Alignment.Center).size(100.dp)
                )

                // Top row: back + share, floating over the image
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CircleIconButton(icon = Icons.Default.ArrowBack, onClick = onBack)
                    CircleIconButton(icon = Icons.Default.Share, onClick = {})
                }

                // Category badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.92f),
                    modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)
                ) {
                    Text(
                        "Nature",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Content card overlapping the hero
        item {
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth().offset(y = (-20).dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Hidden Waterfall Trail",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(4) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldStar, modifier = Modifier.size(16.dp))
                                }
                                Icon(Icons.Default.StarBorder, contentDescription = null, tint = GoldStar, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "4.0 · 45 reviews",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        VoteButton(
                            score = score,
                            userVote = userVote,
                            onUpvote = {
                                score += if (userVote == 1) -1 else if (userVote == -1) 2 else 1
                                userVote = if (userVote == 1) 0 else 1
                            },
                            onDownvote = {
                                score += if (userVote == -1) 1 else if (userVote == 1) -2 else -1
                                userVote = if (userVote == -1) 0 else -1
                            }
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Info chips row
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoChip(label = "2h 15m")
                        InfoChip(label = "4.2 km")
                        InfoChip(label = "Moderate")
                    }

                    Spacer(Modifier.height(24.dp))
                    Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "A secluded waterfall reachable after a 2hr forest hike. Bring good shoes and plenty of water — the last stretch is a steep scramble but the view is unforgettable. Best visited early morning to catch the light through the canopy.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )

                    Spacer(Modifier.height(28.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Comments (${comments.value.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text("Share your experience...") },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (commentText.isNotBlank()) {
                                    comments.value = comments.value + Comment(
                                        id = "c${comments.value.size + 1}",
                                        locationId = locationId,
                                        userId = "me",
                                        userName = "You",
                                        text = commentText
                                    )
                                    commentText = ""
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }

        items(comments.value) { comment ->
            CommentRow(comment)
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun CircleIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
private fun InfoChip(label: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun CommentRow(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                comment.userName.take(1),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(comment.userName, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(2.dp))
            Text(
                comment.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}