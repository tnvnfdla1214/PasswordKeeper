package com.passwordkeeper.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.domain.usecase.GetPasswordByIdUseCase
import com.passwordkeeper.domain.usecase.InsertPasswordUseCase
import com.passwordkeeper.domain.usecase.UpdatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordFormViewModel @Inject constructor(
    private val insertPasswordUseCase: InsertPasswordUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val getPasswordByIdUseCase: GetPasswordByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val passwordId: Long? = savedStateHandle.get<Long>("passwordId")

    private val _serviceName = MutableStateFlow("")
    val serviceName: StateFlow<String> = _serviceName.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _memo = MutableStateFlow("")
    val memo: StateFlow<String> = _memo.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        passwordId?.let { id ->
            loadPassword(id)
        }
    }

    private fun loadPassword(id: Long) {
        viewModelScope.launch {
            getPasswordByIdUseCase(id)?.let { password ->
                _serviceName.value = password.serviceName
                _userId.value = password.userId
                _password.value = password.password
                _memo.value = password.memo
                _isEditMode.value = true
            }
        }
    }

    fun onServiceNameChange(value: String) {
        _serviceName.value = value
    }

    fun onUserIdChange(value: String) {
        _userId.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onMemoChange(value: String) {
        _memo.value = value
    }

    fun savePassword(onSuccess: () -> Unit) {
        if (_serviceName.value.isBlank() || _userId.value.isBlank() || _password.value.isBlank()) {
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val password = Password(
                    id = passwordId ?: 0,
                    serviceName = _serviceName.value,
                    userId = _userId.value,
                    password = _password.value,
                    memo = _memo.value,
                    lastModifiedAt = System.currentTimeMillis()
                )

                if (isEditMode.value) {
                    updatePasswordUseCase(password)
                } else {
                    insertPasswordUseCase(password)
                }

                onSuccess()
            } finally {
                _isSaving.value = false
            }
        }
    }
}
