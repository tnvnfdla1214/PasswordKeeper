package com.passwordkeeper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memos")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val memo: String = "",
    val activityTime: Long = System.currentTimeMillis()
)
