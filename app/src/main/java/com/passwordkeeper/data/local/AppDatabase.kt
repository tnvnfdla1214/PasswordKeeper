package com.passwordkeeper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.passwordkeeper.data.local.dao.PasswordDao
import com.passwordkeeper.data.local.dao.MemoDao
import com.passwordkeeper.data.local.entity.PasswordEntity
import com.passwordkeeper.data.local.entity.MemoEntity

@Database(
    entities = [PasswordEntity::class, MemoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
    abstract fun memoDao(): MemoDao

    companion object {
        const val DATABASE_NAME = "password_keeper_db"
    }
}
