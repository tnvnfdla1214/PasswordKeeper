package com.passwordkeeper.data.local.datastore

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveCountDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "app_preferences",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val SAVE_COUNT_KEY = "save_count"
    }

    fun getSaveCount(): Int {
        return sharedPreferences.getInt(SAVE_COUNT_KEY, 0)
    }

    fun incrementSaveCount(): Int {
        val currentCount = getSaveCount()
        val newCount = currentCount + 1
        sharedPreferences.edit().putInt(SAVE_COUNT_KEY, newCount).apply()
        return newCount
    }
}
