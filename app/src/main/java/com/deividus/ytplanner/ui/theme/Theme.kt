package com.deividus.ytplanner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.deividus.ytplanner.domain.model.VideoStatus

private val YtPlannerDarkColorScheme = darkColorScheme(
    primary = YtRed,
    onPrimary = Color.White,
    primaryContainer = YtRedContainer,
    onPrimaryContainer = Color.White,
    secondary = StatusProgramado,
    onSecondary = Color.White,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceMutedDark,
    outline = OutlineDark,
    error = Color(0xFFCF6679)
)

/** Maps a lifecycle stage to its semantic accent color, per the app's design guide. */
fun VideoStatus.color(): Color = when (this) {
    VideoStatus.IDEA -> StatusIdea
    VideoStatus.GUION -> StatusGuion
    VideoStatus.EDICION -> StatusEdicion
    VideoStatus.PROGRAMADO -> StatusProgramado
    VideoStatus.PUBLICADO -> StatusPublicado
}

@Composable
fun YtPlannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = YtPlannerDarkColorScheme,
        typography = YtPlannerTypography,
        content = content
    )
}
