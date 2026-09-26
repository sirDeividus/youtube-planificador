package com.deividus.ytplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val colorHex: String,
    val description: String? = null
)
