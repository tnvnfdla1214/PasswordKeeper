package com.passwordkeeper.domain.usecase

import javax.inject.Inject

class UpdateLastAccessedUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.updateLastAccessedAt(id)
    }
}
