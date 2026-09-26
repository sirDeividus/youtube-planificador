package com.deividus.ytplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.deividus.ytplanner.domain.model.VideoStatus
import java.util.UUID

@Entity(
    tableName = "videos",
    foreignKeys = [
        ForeignKey(
            entity = ChannelEntity::class,
            parentColumns = ["id"],
            childColumns = ["channelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("channelId")]
)
data class VideoEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val channelId: String,
    val title: String,
    val notes: String? = null,
    val status: VideoStatus,
    val scheduledDate: Long,
    val publishedDate: Long? = null,
    val youtubeUrl: String? = null
)
