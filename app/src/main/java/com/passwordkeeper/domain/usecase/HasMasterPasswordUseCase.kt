package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.repository.AuthRepository
import javax.inject.Inject

class HasMasterPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.hasMasterPassword()
    }
}
