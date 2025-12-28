package com.passwordkeeper.domain.usecase

import com.passwordkeeper.data.local.entity.ItemType
import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchItemsByTypeUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(type: ItemType, query: String): Flow<List<Item>> {
        return repository.searchItemsByType(type, query)
    }
}
