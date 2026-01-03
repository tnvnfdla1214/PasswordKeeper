package com.passwordkeeper.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.domain.usecase.VerifyMasterPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val verifyMasterPasswordUseCase: VerifyMasterPasswordUseCase
) : ViewModel() {
    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    fun validatePassword(password: String) {
        viewModelScope.launch {
            try {
                val isValid = verifyMasterPasswordUseCase(password)
                if (isValid) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("비밀번호가 일치하지 않습니다")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun setAuthErrorIdle() {
        _authState.value = AuthState.ErrorIdle
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
    data object ErrorIdle : AuthState()
}