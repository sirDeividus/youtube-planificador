package com.deividus.ytplanner.domain.repository

import com.deividus.ytplanner.domain.model.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    fun getChannels(): Flow<List<Channel>>
    suspend fun getChannelById(id: String): Channel?
    suspend fun upsertChannel(channel: Channel)
    suspend fun deleteChannel(channel: Channel)
}
