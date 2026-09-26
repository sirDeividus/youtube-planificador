package com.deividus.ytplanner.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.deividus.ytplanner.ui.board.BoardScreen
import com.deividus.ytplanner.ui.board.VideoEditScreen
import com.deividus.ytplanner.ui.calendar.CalendarScreen
import com.deividus.ytplanner.ui.channels.ChannelEditScreen
import com.deividus.ytplanner.ui.channels.ChannelListScreen

@Composable
fun YtPlannerNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { PlannerBottomBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Board.route,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {
            composable(Screen.Board.route) {
                BoardScreen(
                    onAddVideo = { navController.navigate(Screen.VideoEdit.createRoute()) },
                    onEditVideo = { videoId -> navController.navigate(Screen.VideoEdit.createRoute(videoId)) }
                )
            }

            composable(Screen.Calendar.route) {
                CalendarScreen(
                    onEditVideo = { videoId -> navController.navigate(Screen.VideoEdit.createRoute(videoId)) }
                )
            }

            composable(Screen.Channels.route) {
                ChannelListScreen(
                    onAddChannel = { navController.navigate(Screen.ChannelEdit.createRoute()) },
                    onEditChannel = { channelId -> navController.navigate(Screen.ChannelEdit.createRoute(channelId)) }
                )
            }

            composable(
                route = Screen.VideoEdit.route,
                arguments = listOf(
                    navArgument(Screen.VideoEdit.ARG_VIDEO_ID) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
                VideoEditScreen(
                    onSaved = { navController.popBackStack() },
                    onDeleted = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.ChannelEdit.route,
                arguments = listOf(
                    navArgument(Screen.ChannelEdit.ARG_CHANNEL_ID) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
                ChannelEditScreen(
                    onSaved = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun PlannerBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination ?: return

    val topLevelRoutes = bottomNavItems.map { it.screen.route }
    if (destination.hierarchy.none { it.route in topLevelRoutes }) return

    NavigationBar {
        bottomNavItems.forEach { item ->
            val selected = destination.hierarchy.any { it.route == item.screen.route }
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
