package com.deividus.ytplanner.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.deividus.ytplanner.ui.theme.ChannelColorPalette

/** Parses a "#RRGGBB" or "#AARRGGBB" string, falling back to the first palette color. */
fun String?.toColorOrDefault(): Color = try {
    if (this.isNullOrBlank()) ChannelColorPalette.first() else Color(android.graphics.Color.parseColor(this))
} catch (e: IllegalArgumentException) {
    ChannelColorPalette.first()
}

fun Color.toHex(): String = String.format("#%06X", 0xFFFFFF and toArgb())
