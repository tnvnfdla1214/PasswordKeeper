package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.repository.ItemRepository
import javax.inject.Inject

class InsertItemUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(item: Item): Long {
        return repository.insertItem(item)
    }
}
