package com.passwordkeeper.presentation.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.text.font.FontWeight
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
) {
    val items by viewModel.items.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BackHandler(enabled = isSearchFocused) {
        focusManager.clearFocus()
    }

    Scaffold(
        bottomBar = {
            HomeBottomScaffoldButton()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0xFF355F9B),
                        0.2f to Color(0xFF122035)
                    )
                )
                .padding(paddingValues)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusManager.clearFocus() }
        ) {
            HomeContentSection(
                itemsSize = items.size,
                isSearchFocused = isSearchFocused,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                onSearchFocusChanged = { isSearchFocused = it },
                passwords = items,
                focusManager = focusManager,
                onItemClick = { itemId ->
                    focusManager.clearFocus()
                    viewModel.updateLastAccessed(itemId)
                    onItemClick(itemId)
                }
            )
        }
    }
}

@Composable
fun BoxScope.HomeContentSection(
    itemsSize: Int,
    isSearchFocused: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    passwords: List<Password>,
    focusManager: androidx.compose.ui.focus.FocusManager,
    onItemClick: (Long) -> Unit
) {
    var headerHeight by remember { mutableStateOf(0.dp) }
    var textHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val textSpacer = 21.dp
    val contentOffsetY by animateDpAsState(
        targetValue = if (isSearchFocused) -textHeight - textSpacer else textSpacer,
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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = headerHeight + contentOffsetY),
            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.background
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
                    modifier = Modifier.padding(top = 16.dp)
                )

                HomeDeleteButton(
                    modifier = Modifier.padding(top = 24.dp),
                    focusManager = focusManager
                )

                HomeItemList(
                    modifier = Modifier.weight(1f),
                    passwords = passwords,
                    searchQuery = searchQuery,
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
fun HomeDeleteButton(
    modifier: Modifier,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = { /* 삭제 로직 추가 */ }) {
            Text("삭제", color = Color.Gray, fontSize = 14.sp)
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        contentAlignment = Alignment.Center
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
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = passwords,
            key = { it.id }
        ) { password ->
            PasswordListItem(
                password = password,
                onClick = { onItemClick(password.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HomeBottomScaffoldButton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 49.dp)
    ) {
        Button(
            onClick = {
                //Todo : 버튼 클릭 시 Edit 화면으로 바로 이동
            },
            modifier = Modifier.fillMaxWidth(),
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
            Spacer(modifier = Modifier.width(12.dp))
            Text("추가하기", color = Color.White, fontSize = 16.sp)
        }
    }
}