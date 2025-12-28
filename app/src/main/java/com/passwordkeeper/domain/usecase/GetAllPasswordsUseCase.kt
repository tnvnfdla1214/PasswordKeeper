package com.passwordkeeper.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPasswordsUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    operator fun invoke(): Flow<List<Password>> {
        return repository.getAllPasswords()
    }
}
