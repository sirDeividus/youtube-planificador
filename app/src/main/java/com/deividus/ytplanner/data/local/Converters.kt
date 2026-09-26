package com.deividus.ytplanner.data.local

import androidx.room.TypeConverter
import com.deividus.ytplanner.domain.model.VideoStatus

class Converters {
    @TypeConverter
    fun fromVideoStatus(status: VideoStatus): String = status.name

    @TypeConverter
    fun toVideoStatus(value: String): VideoStatus = VideoStatus.valueOf(value)
}
