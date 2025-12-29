package com.passwordkeeper.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun saveMasterPassword(password: String)
    fun getMasterPassword(): Flow<String?>
    suspend fun verifyPassword(inputPassword: String): Boolean
    suspend fun hasMasterPassword(): Boolean
}
