package com.passwordkeeper.domain.usecase

import javax.inject.Inject

class DeletePasswordUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(password: Password) {
        repository.deletePassword(password)
    }
}
