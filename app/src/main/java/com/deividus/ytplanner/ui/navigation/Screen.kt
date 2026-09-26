package com.deividus.ytplanner.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Board : Screen("board")
    data object Calendar : Screen("calendar")
    data object Channels : Screen("channels")

    data object VideoEdit : Screen("video_edit?videoId={videoId}") {
        const val ARG_VIDEO_ID = "videoId"
        fun createRoute(videoId: String? = null) = "video_edit?videoId=${videoId.orEmpty()}"
    }

    data object ChannelEdit : Screen("channel_edit?channelId={channelId}") {
        const val ARG_CHANNEL_ID = "channelId"
        fun createRoute(channelId: String? = null) = "channel_edit?channelId=${channelId.orEmpty()}"
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Board, "Planificación", Icons.Filled.Dashboard),
    BottomNavItem(Screen.Calendar, "Calendario", Icons.Filled.CalendarMonth),
    BottomNavItem(Screen.Channels, "Canales", Icons.Filled.SmartDisplay)
)
