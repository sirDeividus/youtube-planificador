package com.deividus.ytplanner.ui.board

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deividus.ytplanner.domain.model.Channel
import com.deividus.ytplanner.domain.model.Video
import com.deividus.ytplanner.domain.model.VideoStatus
import com.deividus.ytplanner.domain.repository.ChannelRepository
import com.deividus.ytplanner.domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class VideoEditUiState(
    val videoId: String? = null,
    val title: String = "",
    val notes: String = "",
    val channelId: String? = null,
    val status: VideoStatus = VideoStatus.IDEA,
    val scheduledDate: Long = System.currentTimeMillis(),
    val publishedDate: Long? = null,
    val youtubeUrl: String = "",
    val channels: List<Channel> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false
) {
    val isTitleValid: Boolean get() = title.isNotBlank()
    val isValid: Boolean get() = isTitleValid && channelId != null
    val selectedChannel: Channel? get() = channels.find { it.id == channelId }
}

@HiltViewModel
class VideoEditViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val channelRepository: ChannelRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoEditUiState())
    val uiState: StateFlow<VideoEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            channelRepository.getChannels().collect { channels ->
                _uiState.value = _uiState.value.copy(
                    channels = channels,
                    channelId = _uiState.value.channelId ?: channels.firstOrNull()?.id
                )
            }
        }

        val videoId: String? = savedStateHandle["videoId"]
        if (!videoId.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(videoId = videoId, isLoading = true)
            viewModelScope.launch {
                videoRepository.getVideoById(videoId)?.let { video ->
                    _uiState.value = _uiState.value.copy(
                        title = video.title,
                        notes = video.notes.orEmpty(),
                        channelId = video.channelId,
                        status = video.status,
                        scheduledDate = video.scheduledDate,
                        publishedDate = video.publishedDate,
                        youtubeUrl = video.youtubeUrl.orEmpty(),
                        isLoading = false
                    )
                } ?: run {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun onNotesChange(value: String) {
        _uiState.value = _uiState.value.copy(notes = value)
    }

    fun onChannelChange(channelId: String) {
        _uiState.value = _uiState.value.copy(channelId = channelId)
    }

    fun onStatusChange(status: VideoStatus) {
        val state = _uiState.value
        val publishedDate = when {
            status == VideoStatus.PUBLICADO && state.publishedDate == null -> System.currentTimeMillis()
            status != VideoStatus.PUBLICADO -> null
            else -> state.publishedDate
        }
        _uiState.value = state.copy(status = status, publishedDate = publishedDate)
    }

    fun onScheduledDateChange(millis: Long) {
        _uiState.value = _uiState.value.copy(scheduledDate = millis)
    }

    fun onYoutubeUrlChange(value: String) {
        _uiState.value = _uiState.value.copy(youtubeUrl = value)
    }

    fun save() {
        val state = _uiState.value
        val channelId = state.channelId ?: return
        if (!state.isTitleValid) return
        viewModelScope.launch {
            videoRepository.upsertVideo(
                Video(
                    id = state.videoId ?: UUID.randomUUID().toString(),
                    channelId = channelId,
                    title = state.title.trim(),
                    notes = state.notes.trim().ifBlank { null },
                    status = state.status,
                    scheduledDate = state.scheduledDate,
                    publishedDate = state.publishedDate,
                    youtubeUrl = state.youtubeUrl.trim().ifBlank { null }
                )
            )
            _uiState.value = state.copy(isSaved = true)
        }
    }

    fun delete() {
        val state = _uiState.value
        val channelId = state.channelId ?: return
        val videoId = state.videoId ?: return
        viewModelScope.launch {
            videoRepository.deleteVideo(
                Video(
                    id = videoId,
                    channelId = channelId,
                    title = state.title,
                    status = state.status,
                    scheduledDate = state.scheduledDate
                )
            )
            _uiState.value = state.copy(isDeleted = true)
        }
    }
}
