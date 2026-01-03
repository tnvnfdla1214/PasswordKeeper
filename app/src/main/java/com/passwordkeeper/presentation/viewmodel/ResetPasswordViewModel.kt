package com.passwordkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.domain.usecase.SaveMasterPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val saveMasterPasswordUseCase: SaveMasterPasswordUseCase
) : ViewModel() {

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
