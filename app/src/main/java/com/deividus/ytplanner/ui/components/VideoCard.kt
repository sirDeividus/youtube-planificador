package com.deividus.ytplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.deividus.ytplanner.domain.model.VideoWithChannel
import com.deividus.ytplanner.util.formatShortDate
import com.deividus.ytplanner.util.toColorOrDefault

@Composable
fun VideoCard(
    item: VideoWithChannel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val channelColor = item.channel?.colorHex.toColorOrDefault()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .border(1.dp, channelColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = item.video.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(modifier = Modifier.padding(top = 8.dp)) {
            ChannelLabel(
                name = item.channel?.name ?: "Sin canal",
                color = channelColor
            )
        }

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = item.video.scheduledDate.formatShortDate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
