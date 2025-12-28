package com.passwordkeeper.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.data.local.entity.ItemType
import com.passwordkeeper.domain.model.Item
import com.passwordkeeper.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onItemClick: (Long) -> Unit,
    onAddClick: (ItemType) -> Unit // 타입을 전달
) {
    val items by viewModel.items.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()

    var showTypeMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("비밀번호 저장소") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            // 타입 선택 메뉴와 함께 FAB
            Column(
                horizontalAlignment = Alignment.End
            ) {
                if (showTypeMenu) {
                    FloatingActionButton(
                        onClick = {
                            onAddClick(ItemType.PASSWORD)
                            showTypeMenu = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text("비밀번호", modifier = Modifier.padding(horizontal = 16.dp))
                    }

                    FloatingActionButton(
                        onClick = {
                            onAddClick(ItemType.MEMO)
                            showTypeMenu = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text("메모", modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }

                FloatingActionButton(
                    onClick = { showTypeMenu = !showTypeMenu }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "추가")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 검색창
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("제목, 아이디, 내용 검색") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "검색")
                },
                singleLine = true
            )

            // 타입 필터 칩
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedType == null,
                        onClick = { viewModel.onTypeSelected(null) },
                        label = { Text("전체") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedType == ItemType.PASSWORD,
                        onClick = { viewModel.onTypeSelected(ItemType.PASSWORD) },
                        label = { Text("비밀번호") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedType == ItemType.MEMO,
                        onClick = { viewModel.onTypeSelected(ItemType.MEMO) },
                        label = { Text("메모") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 아이템 리스트
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) "저장된 항목이 없습니다" else "검색 결과가 없습니다",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        when (item) {
                            is Item.Password -> PasswordListItem(
                                password = item,
                                onClick = {
                                    viewModel.updateLastAccessed(item.id)
                                    onItemClick(item.id)
                                }
                            )
                            is Item.Memo -> MemoListItem(
                                memo = item,
                                onClick = {
                                    viewModel.updateLastAccessed(item.id)
                                    onItemClick(item.id)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordListItem(
    password: Item.Password,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = password.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "비밀번호",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "아이디: ${password.userId.take(20)}${if (password.userId.length > 20) "..." else ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
            if (password.memo.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = password.memo.take(30) + if (password.memo.length > 30) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun MemoListItem(
    memo: Item.Memo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memo.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = "메모",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = memo.content.take(50) + if (memo.content.length > 50) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}
