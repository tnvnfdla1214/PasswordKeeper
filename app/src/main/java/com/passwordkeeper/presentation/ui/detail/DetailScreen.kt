package com.passwordkeeper.presentation.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.domain.usecase.DeletePasswordUseCase
import com.passwordkeeper.domain.usecase.GetPasswordByIdUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPasswordByIdUseCase: GetPasswordByIdUseCase,
    private val deletePasswordUseCase: DeletePasswordUseCase
) : ViewModel() {

    private val _password = MutableStateFlow<Password?>(null)
    val password: StateFlow<Password?> = _password.asStateFlow()

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword.asStateFlow()

    fun loadPassword(id: Long) {
        viewModelScope.launch {
            _password.value = getPasswordByIdUseCase(id)
        }
    }

    fun togglePasswordVisibility() {
        _showPassword.value = !_showPassword.value
    }

    fun deletePassword(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _password.value?.let { password ->
                deletePasswordUseCase(password)
                onDeleted()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit
) {
    val password by viewModel.password.collectAsState()
    val showPassword by viewModel.showPassword.collectAsState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showCopySnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // passwordId를 savedStateHandle에서 가져와야 함
        // 임시로 1L 사용
        viewModel.loadPassword(1L)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상세 정보") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { password?.let { onEditClick(it.id) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "수정")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "삭제")
                    }
                }
            )
        },
        snackbarHost = {
            if (showCopySnackbar) {
                Snackbar(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("복사되었습니다")
                }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(1000)
                    showCopySnackbar = false
                }
            }
        }
    ) { paddingValues ->
        password?.let { pwd ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // 서비스명
                Text(
                    text = "서비스명",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = pwd.serviceName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 아이디
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                copyToClipboard(context, pwd.userId)
                                showCopySnackbar = true
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "아이디",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = pwd.userId,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "복사",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 비밀번호
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                copyToClipboard(context, pwd.password)
                                showCopySnackbar = true
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "비밀번호",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = pwd.password,
                                style = MaterialTheme.typography.bodyLarge,
                                visualTransformation = if (showPassword) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                            )
                        }
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "복사",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (pwd.memo.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "메모",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = pwd.memo,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("비밀번호 삭제") },
            text = { Text("정말 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePassword(onBackClick)
                        showDeleteDialog = false
                    }
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("password", text)
    clipboard.setPrimaryClip(clip)
}
