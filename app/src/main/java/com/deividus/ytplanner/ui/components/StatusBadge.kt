package com.deividus.ytplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deividus.ytplanner.domain.model.VideoStatus
import com.deividus.ytplanner.ui.theme.color

@Composable
fun StatusBadge(status: VideoStatus, modifier: Modifier = Modifier) {
    val accent = status.color()
    Text(
        text = status.displayName,
        style = MaterialTheme.typography.labelSmall,
        color = accent,
        modifier = modifier
            .background(accent.copy(alpha = 0.16f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
