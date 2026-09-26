package com.deividus.ytplanner.domain.repository

import com.deividus.ytplanner.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getVideos(): Flow<List<Video>>
    suspend fun getVideoById(id: String): Video?
    suspend fun upsertVideo(video: Video)
    suspend fun deleteVideo(video: Video)
}
