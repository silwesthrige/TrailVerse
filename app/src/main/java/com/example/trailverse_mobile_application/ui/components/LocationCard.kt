package com.example.trailverse_mobile_application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.trailverse_mobile_application.model.Location
import com.example.trailverse_mobile_application.ui.theme.GoldStar
import com.example.trailverse_mobile_application.ui.theme.categoryBrush

@Composable
fun LocationCard(
    location: Location,
    userVote: Int,
    onClick: () -> Unit,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalVotes = location.upvotes + location.downvotes
    val ratio = if (totalVotes > 0) location.upvotes.toFloat() / totalVotes else 0f
    val filledStars = (ratio * 5).toInt().coerceIn(0, 5)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(categoryBrush(location.category))
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.align(Alignment.Center).height(52.dp)
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.92f),
                modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)
            ) {
                Text(
                    text = location.category,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(modifier = Modifier.padding(top = 3.dp)) {
                        repeat(5) { i ->
                            Icon(
                                if (i < filledStars) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = GoldStar,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
                VoteButton(
                    score = location.score,
                    userVote = userVote,
                    onUpvote = onUpvote,
                    onDownvote = onDownvote
                )
            }
            Text(
                text = location.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}