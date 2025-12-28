package com.passwordkeeper.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.data.local.entity.ItemType
import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.usecase.GetItemByIdUseCase
import com.passwordkeeper.domain.usecase.InsertItemUseCase
import com.passwordkeeper.domain.usecase.UpdateItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemFormViewModel @Inject constructor(
    private val insertItemUseCase: InsertItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val getItemByIdUseCase: GetItemByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: Long? = savedStateHandle.get<Long>("itemId")
    private val initialType: String? = savedStateHandle.get<String>("type")

    private val _itemType = MutableStateFlow(
        when (initialType) {
            "MEMO" -> ItemType.MEMO
            "PASSWORD" -> ItemType.PASSWORD
            else -> ItemType.PASSWORD // 기본값
        }
    )
    val itemType: StateFlow<ItemType> = _itemType.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    // Password 타입 필드
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // Memo 타입 필드
    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()

    // 공통 필드
    private val _memo = MutableStateFlow("")
    val memo: StateFlow<String> = _memo.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        itemId?.let { id ->
            loadItem(id)
        }
    }

    private fun loadItem(id: Long) {
        viewModelScope.launch {
            getItemByIdUseCase(id)?.let { item ->
                when (item) {
                    is Item.Password -> {
                        _itemType.value = ItemType.PASSWORD
                        _title.value = item.title
                        _userId.value = item.userId
                        _password.value = item.password
                        _memo.value = item.memo
                    }
                    is Item.Memo -> {
                        _itemType.value = ItemType.MEMO
                        _title.value = item.title
                        _content.value = item.content
                        _memo.value = item.memo
                    }
                }
                _isEditMode.value = true
            }
        }
    }

    fun onItemTypeChange(type: ItemType) {
        if (!isEditMode.value) { // 수정 모드에서는 타입 변경 불가
            _itemType.value = type
        }
    }

    fun onTitleChange(value: String) {
        _title.value = value
    }

    fun onUserIdChange(value: String) {
        _userId.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onContentChange(value: String) {
        _content.value = value
    }

    fun onMemoChange(value: String) {
        _memo.value = value
    }

    fun saveItem(onSuccess: () -> Unit) {
        // 필수 필드 검증
        when (itemType.value) {
            ItemType.PASSWORD -> {
                if (_title.value.isBlank() || _userId.value.isBlank() || _password.value.isBlank()) {
                    return
                }
            }
            ItemType.MEMO -> {
                if (_title.value.isBlank() || _content.value.isBlank()) {
                    return
                }
            }
        }

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val item = when (itemType.value) {
                    ItemType.PASSWORD -> Item.Password(
                        id = itemId ?: 0,
                        title = _title.value,
                        userId = _userId.value,
                        password = _password.value,
                        memo = _memo.value,
                        activityTime = System.currentTimeMillis() // 등록/수정 시 업데이트
                    )
                    ItemType.MEMO -> Item.Memo(
                        id = itemId ?: 0,
                        title = _title.value,
                        content = _content.value,
                        memo = _memo.value,
                        activityTime = System.currentTimeMillis() // 등록/수정 시 업데이트
                    )
                }

                if (isEditMode.value) {
                    updateItemUseCase(item)
                } else {
                    insertItemUseCase(item)
                }

                onSuccess()
            } finally {
                _isSaving.value = false
            }
        }
    }
}
