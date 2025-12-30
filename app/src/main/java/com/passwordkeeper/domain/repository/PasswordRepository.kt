package com.passwordkeeper.domain.repository

import com.passwordkeeper.domain.model.Password
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun getAllPassword(): Flow<List<Password>>
    suspend fun getPasswordById(id: Long): Password?
    fun searchPasswords(query: String): Flow<List<Password>>
    suspend fun insertPassword(password: Password): Long
    suspend fun updatePassword(password: Password)
    suspend fun deletePassword(id: Long)
    suspend fun updateLastAccessedAt(id: Long)
    suspend fun getPasswordCount(): Int
}
