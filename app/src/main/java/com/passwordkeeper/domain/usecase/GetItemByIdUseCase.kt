package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.repository.ItemRepository
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(id: Long): Item? {
        return repository.getItemById(id)
    }
}
