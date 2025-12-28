package com.passwordkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passwordkeeper.data.local.entity.ItemType
import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val getItemsByTypeUseCase: GetItemsByTypeUseCase,
    private val searchItemsUseCase: SearchItemsUseCase,
    private val searchItemsByTypeUseCase: SearchItemsByTypeUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val updateLastAccessedUseCase: UpdateLastAccessedUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedType = MutableStateFlow<ItemType?>(null) // null = 전체
    val selectedType: StateFlow<ItemType?> = _selectedType.asStateFlow()

    val items: StateFlow<List<Item>> = combine(
        searchQuery.debounce(300),
        selectedType
    ) { query, type ->
        Pair(query, type)
    }.flatMapLatest { (query, type) ->
        when {
            query.isBlank() && type == null -> getAllItemsUseCase()
            query.isBlank() && type != null -> getItemsByTypeUseCase(type)
            query.isNotBlank() && type == null -> searchItemsUseCase(query)
            query.isNotBlank() && type != null -> searchItemsByTypeUseCase(type, query)
            else -> getAllItemsUseCase()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTypeSelected(type: ItemType?) {
        _selectedType.value = type
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            deleteItemUseCase(item)
        }
    }

    fun updateLastAccessed(id: Long) {
        viewModelScope.launch {
            updateLastAccessedUseCase(id)
        }
    }
}
