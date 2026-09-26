package com.deividus.ytplanner.di

import android.content.Context
import androidx.room.Room
import com.deividus.ytplanner.data.local.AppDatabase
import com.deividus.ytplanner.data.local.dao.ChannelDao
import com.deividus.ytplanner.data.local.dao.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()

    @Provides
    fun provideChannelDao(database: AppDatabase): ChannelDao = database.channelDao()

    @Provides
    fun provideVideoDao(database: AppDatabase): VideoDao = database.videoDao()
}
