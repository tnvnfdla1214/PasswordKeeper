package com.passwordkeeper.data.local.dao

import androidx.room.*
import com.passwordkeeper.data.local.entity.PasswordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM passwords ORDER BY activityTime DESC")
    fun getAllPasswords(): Flow<List<PasswordEntity>>

    @Query("SELECT * FROM passwords WHERE id = :id")
    suspend fun getPasswordById(id: Long): PasswordEntity?

    @Query("""
        SELECT * FROM passwords
        WHERE title LIKE '%' || :query || '%'
        OR userId LIKE '%' || :query || '%'
        OR memo LIKE '%' || :query || '%'
        ORDER BY activityTime DESC
    """)
    fun searchPasswords(query: String): Flow<List<PasswordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordEntity): Long

    @Update
    suspend fun updatePassword(password: PasswordEntity)

    @Delete
    suspend fun deletePassword(password: PasswordEntity)

    @Query("UPDATE passwords SET activityTime = :timestamp WHERE id = :id")
    suspend fun updateActivityTime(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM passwords")
    suspend fun getPasswordCount(): Int
}
