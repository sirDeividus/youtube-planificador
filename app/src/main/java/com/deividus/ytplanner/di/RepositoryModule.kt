package com.deividus.ytplanner.di

import com.deividus.ytplanner.data.repository.ChannelRepositoryImpl
import com.deividus.ytplanner.data.repository.VideoRepositoryImpl
import com.deividus.ytplanner.domain.repository.ChannelRepository
import com.deividus.ytplanner.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

    @Binds
    @Singleton
    abstract fun bindVideoRepository(impl: VideoRepositoryImpl): VideoRepository
}
