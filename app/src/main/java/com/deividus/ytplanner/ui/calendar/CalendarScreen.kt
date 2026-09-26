package com.deividus.ytplanner.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deividus.ytplanner.domain.model.VideoWithChannel
import com.deividus.ytplanner.ui.components.EmptyState
import com.deividus.ytplanner.ui.components.VideoCard
import com.deividus.ytplanner.util.spanishLabel
import com.deividus.ytplanner.util.toColorOrDefault
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    onEditVideo: (String) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Calendario") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MonthHeader(
                yearMonth = uiState.yearMonth,
                onPrevious = viewModel::goToPreviousMonth,
                onNext = viewModel::goToNextMonth
            )

            WeekdayHeaderRow()

            MonthGrid(
                yearMonth = uiState.yearMonth,
                selectedDate = uiState.selectedDate,
                videosByDay = uiState.videosByDay,
                onDayClick = viewModel::selectDate
            )

            SelectedDaySection(
                selectedDate = uiState.selectedDate,
                items = uiState.selectedDayItems,
                onEditVideo = onEditVideo,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MonthHeader(
    yearMonth: YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Filled.ChevronLeft, contentDescription = "Mes anterior")
        }
        Text(
            text = "${yearMonth.month.spanishLabel()} ${yearMonth.year}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = onNext) {
            Icon(Icons.Filled.ChevronRight, contentDescription = "Mes siguiente")
        }
    }
}

@Composable
private fun WeekdayHeaderRow() {
    val labels = listOf("L", "M", "X", "J", "V", "S", "D")
    Row(modifier = Modifier.fillMaxWidth()) {
        labels.forEach { label ->
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    videosByDay: Map<LocalDate, List<VideoWithChannel>>,
    onDayClick: (LocalDate) -> Unit
) {
    val firstOfMonth = yearMonth.atDay(1)
    val leadingBlanks = (firstOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
    val daysInMonth = yearMonth.lengthOfMonth()
    val totalCells = leadingBlanks + daysInMonth
    val trailingBlanks = (7 - totalCells % 7) % 7
    val cells: List<LocalDate?> = buildList {
        repeat(leadingBlanks) { add(null) }
        for (day in 1..daysInMonth) add(yearMonth.atDay(day))
        repeat(trailingBlanks) { add(null) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(modifier = Modifier.weight(1f)) {
                        DayCell(
                            date = date,
                            isSelected = date != null && date == selectedDate,
                            isToday = date != null && date == LocalDate.now(),
                            items = date?.let { videosByDay[it].orEmpty() } ?: emptyList(),
                            onClick = { date?.let(onDayClick) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    items: List<VideoWithChannel>,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(3.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (isSelected) Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.24f))
                else Modifier
            )
            .border(
                width = if (isToday) 1.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = date != null, onClick = onClick)
    ) {
        if (date != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    items.take(3).forEach { item ->
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(item.channel?.colorHex.toColorOrDefault(), CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedDaySection(
    selectedDate: LocalDate,
    items: List<VideoWithChannel>,
    onEditVideo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "${selectedDate.dayOfMonth} de ${selectedDate.month.spanishLabel()}",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        if (items.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.EventBusy,
                title = "Nada para este día",
                subtitle = "No hay vídeos programados ni publicados en esta fecha.",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items, key = { it.video.id }) { item ->
                    VideoCard(item = item, onClick = { onEditVideo(item.video.id) })
                }
            }
        }
    }
}
