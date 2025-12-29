package com.passwordkeeper.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    fun validatePassword(password: String) {
        // 실제로는 SharedPreferences 또는 DataStore에서 저장된 비밀번호와 비교
        viewModelScope.launch {
            try {
                // 예시: 저장된 비밀번호가 "1234"라고 가정
                val savedPassword = "1234"

                if (password == savedPassword) {
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
    object Idle : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}