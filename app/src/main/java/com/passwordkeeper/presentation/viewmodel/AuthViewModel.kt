package com.passwordkeeper.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.domain.usecase.HasMasterPasswordUseCase
import com.passwordkeeper.domain.usecase.SaveMasterPasswordUseCase
import com.passwordkeeper.domain.usecase.VerifyPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val verifyPasswordUseCase: VerifyPasswordUseCase,
    private val saveMasterPasswordUseCase: SaveMasterPasswordUseCase,
    private val hasMasterPasswordUseCase: HasMasterPasswordUseCase
) : ViewModel() {
    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    init {
        checkAndInitializeMasterPassword()
    }

    //Todo : 추후 비밀번호 저장 화면 추가 시 제거되어야 한다.
    private fun checkAndInitializeMasterPassword() {
        viewModelScope.launch {
            try {
                val hasMasterPassword = hasMasterPasswordUseCase()
                if (!hasMasterPassword) {
                    // 마스터 비밀번호가 없으면 "1234"로 초기화
                    saveMasterPasswordUseCase("1234")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("초기화 오류: ${e.message}")
            }
        }
    }

    fun validatePassword(password: String) {
        viewModelScope.launch {
            try {
                val isValid = verifyPasswordUseCase(password)
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
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}