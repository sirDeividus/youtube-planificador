package com.deividus.ytplanner.ui.channels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deividus.ytplanner.ui.components.ConfirmDeleteDialog
import com.deividus.ytplanner.ui.components.EmptyState
import com.deividus.ytplanner.util.toColorOrDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelListScreen(
    onAddChannel: () -> Unit,
    onEditChannel: (String) -> Unit,
    viewModel: ChannelListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var pendingDelete by remember { mutableStateOf<ChannelListItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Canales") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddChannel) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir canal")
            }
        }
    ) { padding ->
        if (!uiState.isLoading && uiState.items.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.SmartDisplay,
                title = "Aún no hay canales",
                subtitle = "Añade tu primer canal de YouTube para empezar a planificar contenido.",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp, padding.calculateTopPadding() + 8.dp, 16.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.items, key = { it.channel.id }) { item ->
                    ChannelRow(
                        item = item,
                        onClick = { onEditChannel(item.channel.id) },
                        onDelete = { pendingDelete = item }
                    )
                }
            }
        }
    }

    pendingDelete?.let { item ->
        ConfirmDeleteDialog(
            title = "Eliminar canal",
            message = "Se eliminará \"${item.channel.name}\" junto con sus ${item.videoCount} vídeo(s) planificados. Esta acción no se puede deshacer.",
            onConfirm = {
                viewModel.deleteChannel(item.channel)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null }
        )
    }
}

@Composable
private fun ChannelRow(
    item: ChannelListItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val color = item.channel.colorHex.toColorOrDefault()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 14.dp)
                .size(40.dp)
                .background(color, CircleShape)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.channel.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            item.channel.description?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "${item.videoCount} vídeo(s) planificados",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar canal",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
