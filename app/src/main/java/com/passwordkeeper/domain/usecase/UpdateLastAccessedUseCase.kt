package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.repository.PasswordRepository
import javax.inject.Inject

class UpdateLastAccessedUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.updateLastAccessedAt(id)
    }
}