package com.deividus.ytplanner.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deividus.ytplanner.domain.model.Channel
import com.deividus.ytplanner.domain.model.VideoStatus
import com.deividus.ytplanner.domain.model.VideoWithChannel
import com.deividus.ytplanner.ui.components.ChannelDot
import com.deividus.ytplanner.ui.components.ConfirmDeleteDialog
import com.deividus.ytplanner.ui.components.EmptyState
import com.deividus.ytplanner.ui.components.VideoCard
import com.deividus.ytplanner.ui.theme.color
import com.deividus.ytplanner.util.toColorOrDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(
    onAddVideo: () -> Unit,
    onEditVideo: (String) -> Unit,
    viewModel: BoardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var pendingDelete by remember { mutableStateOf<VideoWithChannel?>(null) }
    val hasAnyVideo = uiState.columns.values.any { it.isNotEmpty() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Planificación") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddVideo) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir vídeo")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (uiState.channels.isNotEmpty()) {
                ChannelFilterRow(
                    channels = uiState.channels,
                    selectedChannelId = uiState.selectedChannelId,
                    onSelect = viewModel::selectChannel
                )
            }

            if (!uiState.isLoading && !hasAnyVideo) {
                EmptyState(
                    icon = Icons.Filled.VideoLibrary,
                    title = "Sin vídeos todavía",
                    subtitle = "Crea tu primera idea y empieza a mover el contenido por el flujo de producción.",
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(VideoStatus.entries.toList(), key = { it.name }) { status ->
                        BoardColumn(
                            status = status,
                            items = uiState.columns[status].orEmpty(),
                            onCardClick = { onEditVideo(it.video.id) },
                            onAdvance = { item ->
                                nextStatus(status)?.let { viewModel.moveToStatus(item.video, it) }
                            },
                            onDelete = { pendingDelete = it }
                        )
                    }
                }
            }
        }
    }

    pendingDelete?.let { item ->
        ConfirmDeleteDialog(
            title = "Eliminar vídeo",
            message = "Se eliminará \"${item.video.title}\" de la planificación.",
            onConfirm = {
                viewModel.deleteVideo(item.video)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null }
        )
    }
}

private fun nextStatus(current: VideoStatus): VideoStatus? {
    val order = VideoStatus.entries.toList()
    val index = order.indexOf(current)
    return order.getOrNull(index + 1)
}

@Composable
private fun ChannelFilterRow(
    channels: List<Channel>,
    selectedChannelId: String?,
    onSelect: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedChannelId == null,
                onClick = { onSelect(null) },
                label = { Text("Todos") }
            )
        }
        items(channels, key = { it.id }) { channel ->
            FilterChip(
                selected = selectedChannelId == channel.id,
                onClick = { onSelect(channel.id) },
                leadingIcon = { ChannelDot(color = channel.colorHex.toColorOrDefault()) },
                label = { Text(channel.name) }
            )
        }
    }
}

@Composable
private fun BoardColumn(
    status: VideoStatus,
    items: List<VideoWithChannel>,
    onCardClick: (VideoWithChannel) -> Unit,
    onAdvance: (VideoWithChannel) -> Unit,
    onDelete: (VideoWithChannel) -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(status.color().copy(alpha = 0.14f), RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(status.color(), CircleShape)
                    .padding(4.dp)
            )
            Text(
                text = status.displayName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = items.size.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items, key = { it.video.id }) { item ->
                Column {
                    VideoCard(item = item, onClick = { onCardClick(item) })
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (status != VideoStatus.PUBLICADO) {
                            IconButton(onClick = { onAdvance(item) }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowForward,
                                    contentDescription = "Avanzar estado",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        IconButton(onClick = { onDelete(item) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
