package com.passwordkeeper.data.repository

import com.passwordkeeper.data.local.dao.PasswordDao
import com.passwordkeeper.data.local.entity.PasswordEntity
import com.passwordkeeper.domain.model.ItemType
import com.passwordkeeper.domain.model.Password
import com.passwordkeeper.domain.repository.PasswordRepository
import com.passwordkeeper.util.EncryptionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PasswordRepositoryImpl @Inject constructor(
    private val passwordDao: PasswordDao,
    private val encryptionManager: EncryptionManager
) : PasswordRepository {

    override fun getAllPassword(): Flow<List<Password>> {
        return passwordDao.getAllPasswords().map { passwords ->
            passwords.map { it.toDomain(encryptionManager) }
                .sortedByDescending { it.activityTime }
        }
    }

    override suspend fun getPasswordById(id: Long): Password? {
        return passwordDao.getPasswordById(id)?.toDomain(encryptionManager)
    }

    override fun searchPasswords(query: String): Flow<List<Password>> {
        return passwordDao.searchPasswords(query).map { passwords ->
            passwords.map { it.toDomain(encryptionManager) }
                .sortedByDescending { it.activityTime }
        }
    }

    override suspend fun insertPassword(password: Password): Long {
        return passwordDao.insertPassword(password.toEntity())
    }

    override suspend fun updatePassword(password: Password) {
        passwordDao.updatePassword(password.toEntity())
    }

    override suspend fun deletePassword(id: Long) {
        passwordDao.deletePasswordById(id)
    }
    
    override suspend fun updateLastAccessedAt(id: Long) {
        passwordDao.updateActivityTime(id)
    }

    override suspend fun getPasswordCount(): Int {
        return passwordDao.getPasswordCount()
    }

    // Entity to Domain 변환
    private fun PasswordEntity.toDomain(encryptionManager: EncryptionManager): Password {
        return Password(
            id = id,
            title = title,
            userId = userId,
            password = encryptionManager.decrypt(encryptedPassword),
            memo = memo,
            activityTime = activityTime,
        )
    }

    private fun Password.toEntity(): PasswordEntity {
        return PasswordEntity(
            id = id,
            title = title,
            userId = userId,
            encryptedPassword = encryptionManager.encrypt(password),
            memo = memo,
            activityTime = activityTime
        )
    }
}
