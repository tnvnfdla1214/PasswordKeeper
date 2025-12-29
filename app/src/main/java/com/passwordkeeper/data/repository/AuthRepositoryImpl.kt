package com.passwordkeeper.data.repository

import com.passwordkeeper.data.local.datastore.AuthPreferencesDataSource
import com.passwordkeeper.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authPreferencesDataSource: AuthPreferencesDataSource
) : AuthRepository {

    override suspend fun saveMasterPassword(password: String) {
        authPreferencesDataSource.saveMasterPassword(password)
    }

    override fun getMasterPassword(): Flow<String?> {
        return authPreferencesDataSource.getMasterPassword()
    }

    override suspend fun verifyPassword(inputPassword: String): Boolean {
        return authPreferencesDataSource.verifyPassword(inputPassword)
    }

    override suspend fun hasMasterPassword(): Boolean {
        return authPreferencesDataSource.hasMasterPassword()
    }
}
