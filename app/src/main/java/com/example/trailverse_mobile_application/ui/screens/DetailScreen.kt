package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trailverse_mobile_application.model.Comment
import com.example.trailverse_mobile_application.ui.components.VoteButton

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).height(56.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Hidden Waterfall Trail",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
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
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "A secluded waterfall reachable after a 2hr forest hike. Bring good shoes and plenty of water — the last stretch is a steep scramble but the view is unforgettable.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(20.dp))
                    Text("Comments", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text("Add a comment...") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        IconButton(onClick = {
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
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Send")
                        }
                    }
                }
            }
            items(comments.value) { comment ->
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(comment.userName, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                    Text(comment.text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}