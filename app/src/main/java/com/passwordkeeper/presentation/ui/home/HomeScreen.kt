package com.passwordkeeper.presentation.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.passwordkeeper.domain.model.Password
import com.passwordkeeper.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onItemClick: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isDeleteMode by viewModel.isDeleteMode.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()

    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    var screenHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    //Todo : 앱을 켠상태로 홀드 후 다시 들어와서 검색을 누르면 깜박 거림 수정해야 함
    BackHandler(enabled = isSearchFocused || isDeleteMode) {
        when {
            isSearchFocused -> focusManager.clearFocus()
            isDeleteMode -> viewModel.toggleDeleteMode()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.0f to Color(0xFF355F9B),
                    0.2f to Color(0xFF122035)
                )
            )
            .onGloballyPositioned { coordinates ->
                if (screenHeight == 0.dp) {
                    with(density) {
                        screenHeight = coordinates.size.height.toDp()
                    }
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
    ) {

        HomeContentSection(
            itemsSize = items.size,
            isSearchFocused = isSearchFocused,
            isDeleteMode = isDeleteMode,
            selectedItems = selectedItems,
            screenHeight = screenHeight,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onSearchFocusChanged = { isSearchFocused = it },
            onToggleDeleteMode = { viewModel.toggleDeleteMode() },
            onToggleItemSelection = { viewModel.toggleItemSelection(it) },
            passwords = items,
            focusManager = focusManager,
            onItemClick = { itemId ->
                focusManager.clearFocus()
                viewModel.updateLastAccessed(itemId)
                onItemClick(itemId)
            }
        )

        HomeBottomScaffoldButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onAddClick = onAddClick,
            isDeleteMode = isDeleteMode,
            selectedCount = selectedItems.size,
            onDeleteSelectedItemsClick = { viewModel.deleteSelectedItems() }
        )
    }
}

@Composable
fun BoxScope.HomeContentSection(
    itemsSize: Int,
    isSearchFocused: Boolean,
    isDeleteMode: Boolean,
    selectedItems: Set<Long>,
    screenHeight: Dp,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onToggleDeleteMode: () -> Unit,
    onToggleItemSelection: (Long) -> Unit,
    passwords: List<Password>,
    focusManager: androidx.compose.ui.focus.FocusManager,
    onItemClick: (Long) -> Unit
) {
    var headerHeight by remember { mutableStateOf(0.dp) }
    var textHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val textSpacer = 21.dp
    val contentOffsetY by animateDpAsState(
        targetValue = if (isSearchFocused) textHeight + textSpacer else 0.dp,
        label = "contentOffsetY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.TopStart)
    ) {
        Column(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    with(density) {
                        headerHeight = coordinates.size.height.toDp()
                    }
                }
                .padding(bottom = textSpacer)
        ) {
            Spacer(modifier = Modifier.height(108.dp))
            Text(
                "${itemsSize}개의 내 정보가\n안전하게 저장돼 있어요",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        with(density) {
                            textHeight = coordinates.size.height.toDp()
                        }
                    }
                    .padding(start = 21.dp)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(screenHeight - headerHeight + contentOffsetY)
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                HomeSearchField(
                    modifier = Modifier.padding(top = 18.dp),
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    onSearchFocusChanged = onSearchFocusChanged
                )

                HomeAdBanner(
                    modifier = Modifier.height(50.dp).padding(top = 16.dp)
                )

                HomeItemList(
                    modifier = Modifier.weight(1f),
                    passwords = passwords,
                    searchQuery = searchQuery,
                    isDeleteMode = isDeleteMode,
                    selectedItems = selectedItems,
                    onToggleDeleteMode = onToggleDeleteMode,
                    onToggleItemSelection = onToggleItemSelection,
                    focusManager = focusManager,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun PasswordListItem(
    password: Password,
    isDeleteMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDeleteMode && isSelected) {
                Color(0xFFE3F2FD)
            } else {
                Color(0xFFF5F5F5)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(
                    id = if (password.userId.isBlank()) {
                        com.passwordkeeper.R.drawable.ic_memo
                    } else {
                        com.passwordkeeper.R.drawable.ic_password
                    }
                ),
                contentDescription = "아이템 이미지",
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = password.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = password.userId.ifBlank {
                        password.memo
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }

            if (isDeleteMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF355F9B)
                    )
                )
            } else {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = com.passwordkeeper.R.drawable.left_arrow),
                    contentDescription = "왼쪽 화살표 이미지",
                )
            }
        }
    }
}

@Composable
fun HomeSearchField(
    modifier: Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .onFocusChanged { focusState ->
                onSearchFocusChanged(focusState.isFocused)
            },
        placeholder = { Text("계정 이름을 입력해 보세요") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "검색")
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5)
        )
    )
}

@Composable
fun HomeDeleteToggle(
    modifier: Modifier,
    isDeleteMode: Boolean,
    onToggleDeleteMode: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggleDeleteMode
                )
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                modifier = Modifier.size(16.dp),
                checked = isDeleteMode,
                onCheckedChange = { onToggleDeleteMode() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF355F9B)
                )
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("삭제하기", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun HomeAdBanner(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                // TODO: 실제 광고 ID로 교체 필요
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // 테스트 광고 ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun HomeItemList(
    modifier: Modifier = Modifier,
    passwords: List<Password>,
    searchQuery: String,
    isDeleteMode: Boolean,
    selectedItems: Set<Long>,
    onToggleDeleteMode: () -> Unit,
    onToggleItemSelection: (Long) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager,
    onItemClick: (Long) -> Unit
) {
    if (passwords.isEmpty()) {
        HomeEmptyState(
            modifier = modifier,
            searchQuery = searchQuery,
            focusManager = focusManager
        )
    } else {
        HomePasswordList(
            modifier = modifier,
            passwords = passwords,
            isDeleteMode = isDeleteMode,
            selectedItems = selectedItems,
            onToggleDeleteMode = onToggleDeleteMode,
            onToggleItemSelection = onToggleItemSelection,
            focusManager = focusManager,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun HomeEmptyState(
    modifier: Modifier = Modifier,
    searchQuery: String,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 40.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = if (searchQuery.isBlank()) "저장된 항목이 없습니다" else "검색 결과가 없습니다",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HomePasswordList(
    modifier: Modifier = Modifier,
    passwords: List<Password>,
    isDeleteMode: Boolean,
    selectedItems: Set<Long>,
    onToggleDeleteMode: () -> Unit,
    onToggleItemSelection: (Long) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager,
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            HomeDeleteToggle(
                modifier = Modifier.padding(vertical = 12.dp),
                isDeleteMode = isDeleteMode,
                onToggleDeleteMode = onToggleDeleteMode,
                focusManager = focusManager
            )
        }

        items(
            items = passwords,
            key = { it.id }
        ) { password ->
            PasswordListItem(
                password = password,
                isDeleteMode = isDeleteMode,
                isSelected = selectedItems.contains(password.id),
                onClick = {
                    if (isDeleteMode) {
                        onToggleItemSelection(password.id)
                    } else {
                        onItemClick(password.id)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HomeBottomScaffoldButton(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    isDeleteMode: Boolean,
    selectedCount: Int,
    onDeleteSelectedItemsClick: () -> Unit
) {
    if (isDeleteMode) {
        Button(
            onClick = onDeleteSelectedItemsClick,
            modifier = modifier.fillMaxWidth().padding(bottom = 48.dp, start = 16.dp, end = 16.dp),
            enabled = selectedCount > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedCount > 0) Color(0xFF355F9B) else Color.Gray,
                disabledContainerColor = Color.Gray
            ),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("${selectedCount}개", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.size(2.dp))
            Text("삭제하기", color = Color.White, fontSize = 16.sp)
        }
    } else {
        Button(
            onClick = onAddClick,
            modifier = modifier.padding(end = 16.dp, bottom = 48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF355F9B)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "추가하기",
                modifier = Modifier.size(12.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("추가하기", color = Color.White, fontSize = 16.sp)
        }
    }
}