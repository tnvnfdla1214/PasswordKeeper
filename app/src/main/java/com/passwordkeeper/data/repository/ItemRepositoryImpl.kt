package com.passwordkeeper.data.repository

import com.passwordkeeper.data.local.dao.PasswordDao
import com.passwordkeeper.data.local.dao.MemoDao
import com.passwordkeeper.data.local.entity.PasswordEntity
import com.passwordkeeper.data.local.entity.MemoEntity
import com.passwordkeeper.data.local.entity.ItemType
import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.repository.ItemRepository
import com.passwordkeeper.util.EncryptionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val passwordDao: PasswordDao,
    private val memoDao: MemoDao,
    private val encryptionManager: EncryptionManager
) : ItemRepository {

    override fun getAllItems(): Flow<List<Item>> {
        return combine(
            passwordDao.getAllPasswords(),
            memoDao.getAllMemos()
        ) { passwords, memos ->
            val passwordItems = passwords.map { it.toDomain(encryptionManager) }
            val memoItems = memos.map { it.toDomain() }
            (passwordItems + memoItems).sortedByDescending { it.activityTime }
        }
    }

    override fun getItemsByType(type: ItemType): Flow<List<Item>> {
        return when (type) {
            ItemType.PASSWORD -> passwordDao.getAllPasswords().map { passwords ->
                passwords.map { it.toDomain(encryptionManager) }
            }
            ItemType.MEMO -> memoDao.getAllMemos().map { memos ->
                memos.map { it.toDomain() }
            }
        }
    }

    override suspend fun getItemById(id: Long): Item? {
        passwordDao.getPasswordById(id)?.let {
            return it.toDomain(encryptionManager)
        }
        memoDao.getMemoById(id)?.let {
            return it.toDomain()
        }
        return null
    }

    override fun searchItems(query: String): Flow<List<Item>> {
        return combine(
            passwordDao.searchPasswords(query),
            memoDao.searchMemos(query)
        ) { passwords, memos ->
            val passwordItems = passwords.map { it.toDomain(encryptionManager) }
            val memoItems = memos.map { it.toDomain() }
            (passwordItems + memoItems).sortedByDescending { it.activityTime }
        }
    }

    override fun searchItemsByType(type: ItemType, query: String): Flow<List<Item>> {
        return when (type) {
            ItemType.PASSWORD -> passwordDao.searchPasswords(query).map { passwords ->
                passwords.map { it.toDomain(encryptionManager) }
            }
            ItemType.MEMO -> memoDao.searchMemos(query).map { memos ->
                memos.map { it.toDomain() }
            }
        }
    }

    override suspend fun insertItem(item: Item): Long {
        return when (item) {
            is Item.Password -> {
                val entity = PasswordEntity(
                    id = item.id,
                    title = item.title,
                    userId = item.userId,
                    encryptedPassword = encryptionManager.encrypt(item.password),
                    memo = item.memo,
                    activityTime = item.activityTime
                )
                passwordDao.insertPassword(entity)
            }
            is Item.Memo -> {
                val entity = MemoEntity(
                    id = item.id,
                    title = item.title,
                    content = item.content,
                    memo = item.memo,
                    activityTime = item.activityTime
                )
                memoDao.insertMemo(entity)
            }
        }
    }

    override suspend fun updateItem(item: Item) {
        when (item) {
            is Item.Password -> {
                val entity = PasswordEntity(
                    id = item.id,
                    title = item.title,
                    userId = item.userId,
                    encryptedPassword = encryptionManager.encrypt(item.password),
                    memo = item.memo,
                    activityTime = item.activityTime
                )
                passwordDao.updatePassword(entity)
            }
            is Item.Memo -> {
                val entity = MemoEntity(
                    id = item.id,
                    title = item.title,
                    content = item.content,
                    memo = item.memo,
                    activityTime = item.activityTime
                )
                memoDao.updateMemo(entity)
            }
        }
    }

    override suspend fun deleteItem(item: Item) {
        when (item) {
            is Item.Password -> {
                val entity = PasswordEntity(
                    id = item.id,
                    title = item.title,
                    userId = item.userId,
                    encryptedPassword = encryptionManager.encrypt(item.password),
                    memo = item.memo,
                    activityTime = item.activityTime
                )
                passwordDao.deletePassword(entity)
            }
            is Item.Memo -> {
                val entity = MemoEntity(
                    id = item.id,
                    title = item.title,
                    content = item.content,
                    memo = item.memo,
                    activityTime = item.activityTime
                )
                memoDao.deleteMemo(entity)
            }
        }
    }
    
    override suspend fun updateLastAccessedAt(id: Long) {
        passwordDao.updateActivityTime(id)
        memoDao.updateActivityTime(id)
    }

    override suspend fun getItemCount(): Int {
        return passwordDao.getPasswordCount() + memoDao.getMemoCount()
    }

    override suspend fun getItemCountByType(type: ItemType): Int {
        return when (type) {
            ItemType.PASSWORD -> passwordDao.getPasswordCount()
            ItemType.MEMO -> memoDao.getMemoCount()
        }
    }

    // Entity to Domain 변환
    private fun PasswordEntity.toDomain(encryptionManager: EncryptionManager): Item.Password {
        return Item.Password(
            id = id,
            title = title,
            userId = userId,
            password = encryptionManager.decrypt(encryptedPassword),
            memo = memo,
            activityTime = activityTime
        )
    }

    private fun MemoEntity.toDomain(): Item.Memo {
        return Item.Memo(
            id = id,
            title = title,
            content = content,
            memo = memo,
            activityTime = activityTime
        )
    }
}
