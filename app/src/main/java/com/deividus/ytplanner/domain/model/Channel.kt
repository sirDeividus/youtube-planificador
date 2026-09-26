package com.deividus.ytplanner.domain.model

data class Channel(
    val id: String,
    val name: String,
    val colorHex: String,
    val description: String? = null
)
