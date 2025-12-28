package com.passwordkeeper.domain.usecase

import com.passwordkeeper.data.local.entity.ItemType
import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsByTypeUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(type: ItemType): Flow<List<Item>> {
        return repository.getItemsByType(type)
    }
}
