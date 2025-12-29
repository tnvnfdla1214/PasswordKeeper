package com.passwordkeeper.data.local.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_preferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val PASSWORD_KEY = "master_password"
    }

    fun saveMasterPassword(password: String) {
        sharedPreferences.edit().putString(PASSWORD_KEY, password).apply()
    }

    fun getMasterPassword(): Flow<String?> = flow {
        val password = sharedPreferences.getString(PASSWORD_KEY, null)
        emit(password)
    }

    fun verifyPassword(inputPassword: String): Boolean {
        val savedPassword = sharedPreferences.getString(PASSWORD_KEY, null)
        return savedPassword == inputPassword
    }

    fun hasMasterPassword(): Boolean {
        return sharedPreferences.contains(PASSWORD_KEY)
    }
}
