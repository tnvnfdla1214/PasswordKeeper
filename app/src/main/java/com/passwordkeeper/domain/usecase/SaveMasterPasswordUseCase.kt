package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.repository.AuthRepository
import javax.inject.Inject

class SaveMasterPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(password: String) {
        authRepository.saveMasterPassword(password)
    }
}
