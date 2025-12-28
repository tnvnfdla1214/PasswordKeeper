package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(query: String): Flow<List<Item>> {
        return repository.searchItems(query)
    }
}
