package com.deividus.ytplanner.data.repository

import com.deividus.ytplanner.data.local.dao.ChannelDao
import com.deividus.ytplanner.domain.model.Channel
import com.deividus.ytplanner.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val channelDao: ChannelDao
) : ChannelRepository {

    override fun getChannels(): Flow<List<Channel>> =
        channelDao.getChannels().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getChannelById(id: String): Channel? =
        channelDao.getChannelById(id)?.toDomain()

    override suspend fun upsertChannel(channel: Channel) =
        channelDao.upsertChannel(channel.toEntity())

    override suspend fun deleteChannel(channel: Channel) =
        channelDao.deleteChannel(channel.toEntity())
}
