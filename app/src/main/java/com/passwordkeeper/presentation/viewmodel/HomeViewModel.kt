package com.passwordkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.domain.model.Password
import com.passwordkeeper.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPasswordsUseCase: GetAllPasswordsUseCase,
    private val searchItemsUseCase: SearchItemsUseCase,
    private val deleteIPasswordUseCase: DeleteIPasswordUseCase,
    private val updateLastAccessedUseCase: UpdateLastAccessedUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isDeleteMode = MutableStateFlow(false)
    val isDeleteMode: StateFlow<Boolean> = _isDeleteMode.asStateFlow()

    private val _selectedItems = MutableStateFlow<Set<Long>>(emptySet())
    val selectedItems: StateFlow<Set<Long>> = _selectedItems.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: StateFlow<List<Password>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                getAllPasswordsUseCase()
            } else {
                searchItemsUseCase(query)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleDeleteMode() {
        _isDeleteMode.value = !_isDeleteMode.value
        if (!_isDeleteMode.value) {
            _selectedItems.value = emptySet()
        }
    }

    fun toggleItemSelection(id: Long) {
        _selectedItems.value = if (_selectedItems.value.contains(id)) {
            _selectedItems.value - id
        } else {
            _selectedItems.value + id
        }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            val itemsToDelete = items.value.filter { it.id in _selectedItems.value }
            itemsToDelete.forEach { password ->
                deleteIPasswordUseCase(password)
            }
            _selectedItems.value = emptySet()
            _isDeleteMode.value = false
        }
    }

    fun updateLastAccessed(id: Long) {
        viewModelScope.launch {
            updateLastAccessedUseCase(id)
        }
    }
}
