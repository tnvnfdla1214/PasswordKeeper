package com.passwordkeeper.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.domain.usecase.HasMasterPasswordUseCase
import com.passwordkeeper.domain.usecase.SaveMasterPasswordUseCase
import com.passwordkeeper.domain.usecase.VerifyMasterPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val saveMasterPasswordUseCase: SaveMasterPasswordUseCase,
    private val verifyMasterPasswordUseCase: VerifyMasterPasswordUseCase,
    private val hasMasterPasswordUseCase: HasMasterPasswordUseCase
) : ViewModel() {

    private val _oldPassword = mutableStateOf("")
    val oldPassword: State<String> = _oldPassword

    private val _newPassword = mutableStateOf("")
    val newPassword: State<String> = _newPassword

    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: State<String> = _confirmPassword

    private val _showStep = mutableIntStateOf(0) // 0: 기존 비밀번호, 1: 새 비밀번호, 2: 확인 비밀번호
    val showStep: State<Int> = _showStep

    private val _showSuccessDialog = mutableStateOf(false)
    val showSuccessDialog: State<Boolean> = _showSuccessDialog

    private val _showErrorDialog = mutableStateOf(false)
    val showErrorDialog: State<Boolean> = _showErrorDialog

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    init {
        viewModelScope.launch {
            val hasMasterPassword = hasMasterPasswordUseCase()
            _showStep.intValue = if (hasMasterPassword) 0 else 1
        }
    }

    fun onOldPasswordInput(digit: String) {
        if (_oldPassword.value.length < 4) {
            _oldPassword.value += digit
            if (_oldPassword.value.length == 4) {
                validateOldPassword()
            }
        }
    }

    fun onNewPasswordInput(digit: String) {
        if (_newPassword.value.length < 4) {
            _newPassword.value += digit
            if (_newPassword.value.length == 4) {
                _showStep.intValue = 2
            }
        }
    }

    fun onConfirmPasswordInput(digit: String) {
        if (_confirmPassword.value.length < 4) {
            _confirmPassword.value += digit
            if (_confirmPassword.value.length == 4) {
                validateAndSavePassword()
            }
        }
    }

    fun onDeleteClick() {
        when (_showStep.intValue) {
            0 -> _oldPassword.value = _oldPassword.value.dropLast(1)
            1 -> _newPassword.value = _newPassword.value.dropLast(1)
            2 -> _confirmPassword.value = _confirmPassword.value.dropLast(1)
        }
    }

    private fun validateOldPassword() {
        viewModelScope.launch {
            try {
                val isValid = verifyMasterPasswordUseCase(_oldPassword.value)
                if (isValid) {
                    _showStep.intValue = 1
                } else {
                    _errorMessage.value = "기존 비밀번호가 일치하지 않습니다"
                    _showErrorDialog.value = true
                    _oldPassword.value = ""
                }
            } catch (e: Exception) {
                _errorMessage.value = "비밀번호 확인 중 오류가 발생했습니다"
                _showErrorDialog.value = true
                _oldPassword.value = ""
            }
        }
    }

    private fun validateAndSavePassword() {
        viewModelScope.launch {
            if (_newPassword.value != _confirmPassword.value) {
                _errorMessage.value = "비밀번호가 일치하지 않습니다"
                _showErrorDialog.value = true
                _confirmPassword.value = ""
                return@launch
            }

            try {
                saveMasterPasswordUseCase(_newPassword.value)
                _showSuccessDialog.value = true
            } catch (e: Exception) {
                _errorMessage.value = "비밀번호 저장에 실패했습니다"
                _showErrorDialog.value = true
                _confirmPassword.value = ""
            }
        }
    }

    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
    }

    fun dismissErrorDialog() {
        _showErrorDialog.value = false
    }
}
