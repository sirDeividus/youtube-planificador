package com.deividus.ytplanner.data.repository

import com.deividus.ytplanner.data.local.entity.ChannelEntity
import com.deividus.ytplanner.data.local.entity.VideoEntity
import com.deividus.ytplanner.domain.model.Channel
import com.deividus.ytplanner.domain.model.Video

fun ChannelEntity.toDomain() = Channel(
    id = id,
    name = name,
    colorHex = colorHex,
    description = description
)

fun Channel.toEntity() = ChannelEntity(
    id = id,
    name = name,
    colorHex = colorHex,
    description = description
)

fun VideoEntity.toDomain() = Video(
    id = id,
    channelId = channelId,
    title = title,
    notes = notes,
    status = status,
    scheduledDate = scheduledDate,
    publishedDate = publishedDate,
    youtubeUrl = youtubeUrl
)

fun Video.toEntity() = VideoEntity(
    id = id,
    channelId = channelId,
    title = title,
    notes = notes,
    status = status,
    scheduledDate = scheduledDate,
    publishedDate = publishedDate,
    youtubeUrl = youtubeUrl
)
