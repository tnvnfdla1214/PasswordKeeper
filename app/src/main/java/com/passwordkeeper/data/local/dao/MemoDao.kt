package com.passwordkeeper.data.local.dao

import androidx.room.*
import com.passwordkeeper.data.local.entity.MemoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Query("SELECT * FROM memos ORDER BY activityTime DESC")
    fun getAllMemos(): Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos WHERE id = :id")
    suspend fun getMemoById(id: Long): MemoEntity?

    @Query("""
        SELECT * FROM memos
        WHERE title LIKE '%' || :query || '%'
        OR content LIKE '%' || :query || '%'
        OR memo LIKE '%' || :query || '%'
        ORDER BY activityTime DESC
    """)
    fun searchMemos(query: String): Flow<List<MemoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: MemoEntity): Long

    @Update
    suspend fun updateMemo(memo: MemoEntity)

    @Delete
    suspend fun deleteMemo(memo: MemoEntity)

    @Query("UPDATE memos SET activityTime = :timestamp WHERE id = :id")
    suspend fun updateActivityTime(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM memos")
    suspend fun getMemoCount(): Int
}
