package com.studygenie.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val badgeName: String,
    val iconEmoji: String,
    val description: String,
    val unlockDate: Long = System.currentTimeMillis()
)
