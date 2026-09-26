package com.deividus.ytplanner.ui.theme

import androidx.compose.ui.graphics.Color

// Brand
val YtRed = Color(0xFFE50914)
val YtRedContainer = Color(0xFF4D0A0F)

// Dark surfaces
val BackgroundDark = Color(0xFF0F0F0F)
val SurfaceDark = Color(0xFF212121)
val SurfaceVariantDark = Color(0xFF2C2C2C)
val OutlineDark = Color(0xFF3D3D3D)
val OnSurfaceDark = Color(0xFFEDEDED)
val OnSurfaceMutedDark = Color(0xFFA0A0A0)

// Semantic status colors
val StatusIdea = Color(0xFF64B5F6)
val StatusGuion = Color(0xFFFFD54F)
val StatusEdicion = Color(0xFFFFB74D)
val StatusProgramado = Color(0xFFBA68C8)
val StatusPublicado = Color(0xFF81C784)

// Fallback palette offered when creating/editing a channel badge color.
val ChannelColorPalette = listOf(
    0xFFE53935, 0xFFFB8C00, 0xFFFDD835, 0xFF7CB342, 0xFF00897B,
    0xFF039BE5, 0xFF3949AB, 0xFF8E24AA, 0xFFD81B60, 0xFF6D4C41,
    0xFF546E7A, 0xFF00ACC1
).map { Color(it) }
