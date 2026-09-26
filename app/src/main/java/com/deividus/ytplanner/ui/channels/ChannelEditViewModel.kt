package com.deividus.ytplanner.ui.channels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deividus.ytplanner.domain.model.Channel
import com.deividus.ytplanner.domain.repository.ChannelRepository
import com.deividus.ytplanner.ui.theme.ChannelColorPalette
import com.deividus.ytplanner.util.toColorOrDefault
import com.deividus.ytplanner.util.toHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChannelEditUiState(
    val channelId: String? = null,
    val name: String = "",
    val description: String = "",
    val color: Color = ChannelColorPalette.first(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
) {
    val isNameValid: Boolean get() = name.isNotBlank()
}

@HiltViewModel
class ChannelEditViewModel @Inject constructor(
    private val channelRepository: ChannelRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChannelEditUiState())
    val uiState: StateFlow<ChannelEditUiState> = _uiState.asStateFlow()

    init {
        val channelId: String? = savedStateHandle["channelId"]
        if (!channelId.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(channelId = channelId, isLoading = true)
            viewModelScope.launch {
                channelRepository.getChannelById(channelId)?.let { channel ->
                    _uiState.value = _uiState.value.copy(
                        name = channel.name,
                        description = channel.description.orEmpty(),
                        color = channel.colorHex.toColorOrDefault(),
                        isLoading = false
                    )
                } ?: run {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value)
    }

    fun onColorChange(value: Color) {
        _uiState.value = _uiState.value.copy(color = value)
    }

    fun save() {
        val state = _uiState.value
        if (!state.isNameValid) return
        viewModelScope.launch {
            channelRepository.upsertChannel(
                Channel(
                    id = state.channelId ?: UUID.randomUUID().toString(),
                    name = state.name.trim(),
                    colorHex = state.color.toHex(),
                    description = state.description.trim().ifBlank { null }
                )
            )
            _uiState.value = state.copy(isSaved = true)
        }
    }
}
