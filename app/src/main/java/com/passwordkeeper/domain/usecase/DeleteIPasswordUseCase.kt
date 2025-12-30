package com.passwordkeeper.domain.usecase

import com.passwordkeeper.domain.model.Password
import com.passwordkeeper.domain.repository.PasswordRepository
import javax.inject.Inject

class DeleteIPasswordUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(password: Password) {
        repository.deletePassword(password.id)
    }
}
