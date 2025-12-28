package com.passwordkeeper.domain.usecase

import javax.inject.Inject

class InsertPasswordUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(password: Password): Long {
        return repository.insertPassword(password)
    }
}
