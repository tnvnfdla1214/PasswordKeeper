package com.passwordkeeper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val userId: String,
    val encryptedPassword: String,
    val memo: String = "",
    val activityTime: Long = System.currentTimeMillis()
)
