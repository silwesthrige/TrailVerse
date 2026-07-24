package com.example.trailverse_mobile_application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trailverse_mobile_application.ui.theme.DownvoteRed
import com.example.trailverse_mobile_application.ui.theme.UpvoteGreen

@Composable
fun VoteButton(
    score: Int,
    userVote: Int,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = when (userVote) {
        1 -> UpvoteGreen.copy(alpha = 0.12f)
        -1 -> DownvoteRed.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 2.dp)
    ) {
        IconButton(onClick = onUpvote, modifier = Modifier.size(34.dp)) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Upvote",
                tint = if (userVote == 1) UpvoteGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "$score",
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge
        )
        IconButton(onClick = onDownvote, modifier = Modifier.size(34.dp)) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Downvote",
                tint = if (userVote == -1) DownvoteRed else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}