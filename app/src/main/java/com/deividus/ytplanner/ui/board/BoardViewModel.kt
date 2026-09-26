package com.deividus.ytplanner.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deividus.ytplanner.domain.model.Channel
import com.deividus.ytplanner.domain.model.Video
import com.deividus.ytplanner.domain.model.VideoStatus
import com.deividus.ytplanner.domain.model.VideoWithChannel
import com.deividus.ytplanner.domain.repository.ChannelRepository
import com.deividus.ytplanner.domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BoardUiState(
    val channels: List<Channel> = emptyList(),
    val selectedChannelId: String? = null,
    val columns: Map<VideoStatus, List<VideoWithChannel>> = emptyMap(),
    val isLoading: Boolean = true
)

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val channelRepository: ChannelRepository
) : ViewModel() {

    private val selectedChannelId = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(BoardUiState())
    val uiState: StateFlow<BoardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                videoRepository.getVideos(),
                channelRepository.getChannels(),
                selectedChannelId
            ) { videos, channels, selectedId ->
                val channelsById = channels.associateBy { it.id }
                val filtered = if (selectedId == null) videos else videos.filter { it.channelId == selectedId }
                val columns = VideoStatus.entries.associateWith { status ->
                    filtered
                        .filter { it.status == status }
                        .sortedBy { it.scheduledDate }
                        .map { video -> VideoWithChannel(video, channelsById[video.channelId]) }
                }
                BoardUiState(
                    channels = channels,
                    selectedChannelId = selectedId,
                    columns = columns,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun selectChannel(channelId: String?) {
        selectedChannelId.value = channelId
    }

    fun moveToStatus(video: Video, newStatus: VideoStatus) {
        viewModelScope.launch {
            val publishedDate = when {
                newStatus == VideoStatus.PUBLICADO && video.publishedDate == null -> System.currentTimeMillis()
                newStatus != VideoStatus.PUBLICADO -> null
                else -> video.publishedDate
            }
            videoRepository.upsertVideo(video.copy(status = newStatus, publishedDate = publishedDate))
        }
    }

    fun deleteVideo(video: Video) {
        viewModelScope.launch {
            videoRepository.deleteVideo(video)
        }
    }
}
