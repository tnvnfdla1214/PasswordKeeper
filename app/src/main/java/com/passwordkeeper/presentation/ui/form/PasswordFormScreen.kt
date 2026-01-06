package com.passwordkeeper.presentation.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.presentation.ui.components.CustomDialog
import com.passwordkeeper.presentation.ui.components.DialogType
import com.passwordkeeper.presentation.viewmodel.PasswordFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFormScreen(
    viewModel: PasswordFormViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val serviceName by viewModel.serviceName.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val password by viewModel.password.collectAsState()
    val memo by viewModel.memo.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val clipboardManager = LocalClipboardManager.current

    var showSaveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showSaveDialog) {
        CustomDialog(
            type = DialogType.CONFIRM,
            title = "내 정보가 잘 저장 되었어요",
            onDismissRequest = { showSaveDialog = false }
        )
    }

    if (showDeleteDialog) {
        CustomDialog(
            type = DialogType.WARNING,
            title = "정말 삭제하시겠어요?",
            onDismissRequest = { showDeleteDialog = false },
            button1Text = "취소",
            button1Action = { showDeleteDialog = false },
            button2Text = "삭제",
            button2Action = {
                viewModel.deletePassword {
                    showDeleteDialog = false
                    onBackClick()
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if(isEditMode) "내 정보" else "")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "서비스명",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = serviceName,
                    onValueChange = { viewModel.onServiceNameChange(it) },
                    label = { },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = isEditMode,
                    trailingIcon = if (isEditMode && serviceName.isNotBlank()) {
                        {
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(serviceName))
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "복사")
                            }
                        }
                    } else null
                )

                Text(
                    text = "아이디",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 21.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = userId,
                    onValueChange = { viewModel.onUserIdChange(it) },
                    label = { },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = isEditMode,
                    trailingIcon = if (isEditMode && userId.isNotBlank()) {
                        {
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(userId))
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "복사")
                            }
                        }
                    } else null
                )

                Text(
                    text = "비밀번호",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 21.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    readOnly = isEditMode,
                    trailingIcon = if (isEditMode && password.isNotBlank()) {
                        {
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(password))
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "복사")
                            }
                        }
                    } else null
                )

                Text(
                    text = "메모",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 21.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = memo,
                    onValueChange = { viewModel.onMemoChange(it) },
                    label = { },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    readOnly = isEditMode,
                    trailingIcon = if (isEditMode && memo.isNotBlank()) {
                        {
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(memo))
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "복사")
                            }
                        }
                    } else null
                )
            }

            PasswordFormButtons(
                isEditMode = isEditMode,
                isSaving = isSaving,
                serviceName = serviceName,
                userId = userId,
                password = password,
                memo = memo,
                onDeleteClick = { showDeleteDialog = true },
                onSaveClick = {
                    showSaveDialog = true
                    viewModel.savePassword(onSuccess = {})
                },
                onUpdateClick = { viewModel.savePassword(onSuccess = {}) }
            )
        }
    }
}

@Composable
private fun PasswordFormButtons(
    isEditMode: Boolean,
    isSaving: Boolean,
    serviceName: String,
    userId: String,
    password: String,
    memo: String,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUpdateClick: () -> Unit
) {
    val isFormValid = serviceName.isNotBlank() && (
        (userId.isNotBlank() && password.isNotBlank()) ||
        (userId.isBlank() && password.isBlank() && memo.isNotBlank())
    )

    if (isEditMode) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = !isSaving
            ) {
                Text("삭제하기")
            }

            Button(
                onClick = onUpdateClick,
                modifier = Modifier.weight(1f),
                enabled = !isSaving && isFormValid
            ) {
                Text("수정하기")
            }
        }
    } else {
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 48.dp),
            enabled = !isSaving && isFormValid
        ) {
            Text("저장하기")
        }
    }
}
