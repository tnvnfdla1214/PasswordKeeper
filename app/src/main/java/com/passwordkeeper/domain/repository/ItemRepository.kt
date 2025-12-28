package com.passwordkeeper.domain.repository

import com.passwordkeeper.data.local.entity.ItemType
import com.passwordkeeper.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getAllItems(): Flow<List<Item>>
    fun getItemsByType(type: ItemType): Flow<List<Item>>
    suspend fun getItemById(id: Long): Item?
    fun searchItems(query: String): Flow<List<Item>>
    fun searchItemsByType(type: ItemType, query: String): Flow<List<Item>>
    suspend fun insertItem(item: Item): Long
    suspend fun updateItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun updateLastAccessedAt(id: Long)
    suspend fun getItemCount(): Int
    suspend fun getItemCountByType(type: ItemType): Int
}
