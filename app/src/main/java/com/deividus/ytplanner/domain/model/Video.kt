package com.deividus.ytplanner.domain.model

data class Video(
    val id: String,
    val channelId: String,
    val title: String,
    val notes: String? = null,
    val status: VideoStatus,
    val scheduledDate: Long,
    val publishedDate: Long? = null,
    val youtubeUrl: String? = null
)

/**
 * A [Video] paired with the [Channel] it belongs to, for screens that render
 * both at once (board cards, calendar entries) without a second lookup.
 */
data class VideoWithChannel(
    val video: Video,
    val channel: Channel?
)
