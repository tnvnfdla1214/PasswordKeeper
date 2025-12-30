package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyMasterPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(inputPassword: String): Boolean {
        return authRepository.verifyPassword(inputPassword)
    }
}
