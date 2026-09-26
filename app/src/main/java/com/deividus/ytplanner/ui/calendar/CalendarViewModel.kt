package com.deividus.ytplanner.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deividus.ytplanner.domain.model.VideoStatus
import com.deividus.ytplanner.domain.model.VideoWithChannel
import com.deividus.ytplanner.domain.repository.ChannelRepository
import com.deividus.ytplanner.domain.repository.VideoRepository
import com.deividus.ytplanner.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class CalendarUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val videosByDay: Map<LocalDate, List<VideoWithChannel>> = emptyMap(),
    val isLoading: Boolean = true
) {
    val selectedDayItems: List<VideoWithChannel> get() = videosByDay[selectedDate].orEmpty()
}

/** The date a video should be pinned to on the calendar: when it went live once published, otherwise its plan date. */
private fun VideoWithChannel.calendarDate(): LocalDate =
    if (video.status == VideoStatus.PUBLICADO && video.publishedDate != null) {
        video.publishedDate.toLocalDate()
    } else {
        video.scheduledDate.toLocalDate()
    }

@HiltViewModel
class CalendarViewModel @Inject constructor(
    videoRepository: VideoRepository,
    channelRepository: ChannelRepository
) : ViewModel() {

    private val yearMonth = MutableStateFlow(YearMonth.now())
    private val selectedDate = MutableStateFlow(LocalDate.now())

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                videoRepository.getVideos(),
                channelRepository.getChannels(),
                yearMonth,
                selectedDate
            ) { videos, channels, month, selected ->
                val channelsById = channels.associateBy { it.id }
                val grouped = videos
                    .map { video -> VideoWithChannel(video, channelsById[video.channelId]) }
                    .groupBy { it.calendarDate() }
                CalendarUiState(
                    yearMonth = month,
                    selectedDate = selected,
                    videosByDay = grouped,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun goToPreviousMonth() {
        yearMonth.value = yearMonth.value.minusMonths(1)
    }

    fun goToNextMonth() {
        yearMonth.value = yearMonth.value.plusMonths(1)
    }

    fun selectDate(date: LocalDate) {
        selectedDate.value = date
    }
}
