package com.passwordkeeper.domain.usecase

import javax.inject.Inject

class GetPasswordByIdUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(id: Long): Password? {
        return repository.getPasswordById(id)
    }
}
