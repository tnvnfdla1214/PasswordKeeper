package com.passwordkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.domain.usecase.SaveMasterPasswordUseCase
import com.passwordkeeper.domain.usecase.VerifyMasterPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val saveMasterPasswordUseCase: SaveMasterPasswordUseCase,
    private val verifyMasterPasswordUseCase: VerifyMasterPasswordUseCase
) : ViewModel() {

    fun validateOldPassword(password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val isValid = verifyMasterPasswordUseCase(password)
                onResult(isValid)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun savePassword(password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                saveMasterPasswordUseCase(password)
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}
