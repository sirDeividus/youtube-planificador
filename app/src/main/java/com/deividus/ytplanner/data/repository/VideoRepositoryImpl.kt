package com.deividus.ytplanner.data.repository

import com.deividus.ytplanner.data.local.dao.VideoDao
import com.deividus.ytplanner.domain.model.Video
import com.deividus.ytplanner.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val videoDao: VideoDao
) : VideoRepository {

    override fun getVideos(): Flow<List<Video>> =
        videoDao.getVideos().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getVideoById(id: String): Video? =
        videoDao.getVideoById(id)?.toDomain()

    override suspend fun upsertVideo(video: Video) =
        videoDao.upsertVideo(video.toEntity())

    override suspend fun deleteVideo(video: Video) =
        videoDao.deleteVideo(video.toEntity())
}
