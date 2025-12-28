package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(): Flow<List<Item>> {
        return repository.getAllItems()
    }
}
