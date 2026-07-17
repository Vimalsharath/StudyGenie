package com.studygenie.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String, // Firebase UID
    val name: String,
    val email: String,
    val level: Int = 1,
    val xp: Int = 0,
    val streak: Int = 0,
    val coins: Int = 0,
    val productivityScore: Int = 0
)
