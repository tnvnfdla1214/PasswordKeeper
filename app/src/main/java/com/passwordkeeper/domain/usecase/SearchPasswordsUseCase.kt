package com.passwordkeeper.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPasswordsUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    operator fun invoke(query: String): Flow<List<Password>> {
        return repository.searchPasswords(query)
    }
}
