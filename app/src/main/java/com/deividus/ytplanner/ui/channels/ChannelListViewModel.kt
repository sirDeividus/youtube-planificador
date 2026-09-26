package com.deividus.ytplanner.ui.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deividus.ytplanner.domain.model.Channel
import com.deividus.ytplanner.domain.repository.ChannelRepository
import com.deividus.ytplanner.domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChannelListItem(
    val channel: Channel,
    val videoCount: Int
)

data class ChannelListUiState(
    val items: List<ChannelListItem> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val channelRepository: ChannelRepository,
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChannelListUiState())
    val uiState: StateFlow<ChannelListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                channelRepository.getChannels(),
                videoRepository.getVideos()
            ) { channels, videos ->
                val counts = videos.groupingBy { it.channelId }.eachCount()
                ChannelListUiState(
                    items = channels.map { channel ->
                        ChannelListItem(channel, counts[channel.id] ?: 0)
                    },
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun deleteChannel(channel: Channel) {
        viewModelScope.launch {
            channelRepository.deleteChannel(channel)
        }
    }
}
