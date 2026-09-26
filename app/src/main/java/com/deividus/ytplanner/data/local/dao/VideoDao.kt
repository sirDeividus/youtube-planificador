package com.deividus.ytplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deividus.ytplanner.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY scheduledDate ASC")
    fun getVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id")
    suspend fun getVideoById(id: String): VideoEntity?

    @Upsert
    suspend fun upsertVideo(video: VideoEntity)

    @Delete
    suspend fun deleteVideo(video: VideoEntity)
}
