package com.deividus.ytplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.deividus.ytplanner.data.local.dao.ChannelDao
import com.deividus.ytplanner.data.local.dao.VideoDao
import com.deividus.ytplanner.data.local.entity.ChannelEntity
import com.deividus.ytplanner.data.local.entity.VideoEntity

@Database(
    entities = [ChannelEntity::class, VideoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun channelDao(): ChannelDao
    abstract fun videoDao(): VideoDao

    companion object {
        const val DATABASE_NAME = "yt_planner.db"
    }
}
