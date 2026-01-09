package com.passwordkeeper.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.data.local.datastore.SaveCountDataSource
import com.passwordkeeper.domain.model.Password
import com.passwordkeeper.domain.usecase.DeleteIPasswordUseCase
import com.passwordkeeper.domain.usecase.GetPasswordByIdUseCase
import com.passwordkeeper.domain.usecase.InsertPasswordUseCase
import com.passwordkeeper.domain.usecase.UpdatePasswordUseCase
import com.passwordkeeper.presentation.navigation.NavArgs
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
    private val deletePasswordUseCase: DeleteIPasswordUseCase,
    private val saveCountDataSource: SaveCountDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var passwordId: Long? = savedStateHandle.get<Long>(NavArgs.PASSWORD_ID)?.let {
        if (it == -1L) null else it
    }

    private val _serviceName = MutableStateFlow("")
    val serviceName: StateFlow<String> = _serviceName.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _memo = MutableStateFlow("")
    val memo: StateFlow<String> = _memo.asStateFlow()

    private val _formState = MutableStateFlow(
        if (passwordId != null) {
            PasswordFormState.ReadOnly(passwordId!!)
        } else {
            PasswordFormState.Register
        }
    )
    val formState: StateFlow<PasswordFormState> = _formState.asStateFlow()

    init {
        passwordId?.let { id ->
            loadPassword(id)
        }
    }

    fun switchToUpdateMode() {
        val currentState = _formState.value
        if (currentState is PasswordFormState.ReadOnly) {
            _formState.value = PasswordFormState.Update(currentState.id)
        }
    }

    private fun loadPassword(id: Long) {
        viewModelScope.launch {
            getPasswordByIdUseCase(id)?.let { password ->
                _serviceName.value = password.title
                _userId.value = password.userId
                _password.value = password.password
                _memo.value = password.memo
            }
        }
    }

    fun savePassword(onSuccess: (PasswordFormState, Boolean) -> Unit) {
        viewModelScope.launch {
            val password = createPasswordObject()

            when (val state = _formState.value) {
                is PasswordFormState.Register -> {
                    val newId = insertPasswordUseCase(password)
                    passwordId = newId
                    _formState.value = PasswordFormState.ReadOnly(newId)

                    val count = saveCountDataSource.incrementSaveCount()
                    onSuccess(PasswordFormState.Register, shouldShowAd(count))
                }
                is PasswordFormState.Update -> {
                    updatePasswordUseCase(password)
                    _formState.value = PasswordFormState.ReadOnly(state.id)
                    onSuccess(PasswordFormState.Update(state.id), false)
                }
                is PasswordFormState.ReadOnly -> {
                    // ReadOnly 상태에서는 저장하지 않음
                }
            }
        }
    }

    fun deletePassword(onSuccess: () -> Unit) {
        viewModelScope.launch {
            passwordId?.let {
                val password = createPasswordObject()
                deletePasswordUseCase(password)
                onSuccess()
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

    private fun createPasswordObject() = Password(
        id = passwordId ?: 0,
        title = _serviceName.value,
        userId = _userId.value,
        password = _password.value,
        memo = _memo.value,
        activityTime = System.currentTimeMillis()
    )

    private fun shouldShowAd(count : Int): Boolean {
        return count >= 6 && count % 3 == 0
    }
}

sealed class PasswordFormState {
    data object Register : PasswordFormState()
    data class ReadOnly(val id: Long) : PasswordFormState()
    data class Update(val id: Long) : PasswordFormState()
}
