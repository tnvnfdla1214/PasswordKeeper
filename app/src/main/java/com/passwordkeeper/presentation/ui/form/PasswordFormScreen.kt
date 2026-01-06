package com.passwordkeeper.presentation.ui.form

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import com.passwordkeeper.presentation.viewmodel.PasswordFormState
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

    val formState by viewModel.formState.collectAsState()

    LaunchedEffect(formState) {
        Log.d("qweqwe", "Current formState: $formState")
    }

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
                    Text(if(formState is PasswordFormState.ReadOnly) "내 정보" else "")
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
                    readOnly = formState is PasswordFormState.ReadOnly,
                )

                Text(
                    text = "아이디",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 21.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                val userIdInteractionSource = remember { MutableInteractionSource() }
                LaunchedEffect(userIdInteractionSource) {
                    userIdInteractionSource.interactions.collect { interaction ->
                        if (interaction is PressInteraction.Press &&
                            formState is PasswordFormState.ReadOnly &&
                            userId.isNotBlank()) {
                            clipboardManager.setText(AnnotatedString(userId))
                        }
                    }
                }

                OutlinedTextField(
                    value = userId,
                    onValueChange = { viewModel.onUserIdChange(it) },
                    label = { },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = formState is PasswordFormState.ReadOnly,
                    interactionSource = userIdInteractionSource,
                    trailingIcon = if ((formState is PasswordFormState.ReadOnly) && userId.isNotBlank()) {
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

                val passwordInteractionSource = remember { MutableInteractionSource() }
                LaunchedEffect(passwordInteractionSource) {
                    passwordInteractionSource.interactions.collect { interaction ->
                        if (interaction is PressInteraction.Press &&
                            formState is PasswordFormState.ReadOnly &&
                            password.isNotBlank()) {
                            clipboardManager.setText(AnnotatedString(password))
                        }
                    }
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = formState is PasswordFormState.ReadOnly,
                    interactionSource = passwordInteractionSource,
                    trailingIcon = if ((formState is PasswordFormState.ReadOnly) && password.isNotBlank()) {
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
                    readOnly = formState is PasswordFormState.ReadOnly,
                )
            }

            PasswordFormButtons(
                formState = formState,
                serviceName = serviceName,
                userId = userId,
                password = password,
                memo = memo,
                onDeleteClick = { showDeleteDialog = true },
                onSaveClick = {
                    //showSaveDialog = true
                    viewModel.savePassword(onSuccess = {
                        //Todo : 등록이냐 수정이냐 에 따라 분기
                        showSaveDialog = true
                    })
                },
                onEditClick = {
                    viewModel.switchToUpdateMode()
                }
            )
        }
    }
}

@Composable
private fun PasswordFormButtons(
    formState: PasswordFormState,
    serviceName: String,
    userId: String,
    password: String,
    memo: String,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val isFormValid = serviceName.isNotBlank() &&
        (userId.isNotBlank() || password.isNotBlank() || memo.isNotBlank())

    when (formState) {
        is PasswordFormState.Register -> {
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 48.dp),
                enabled = isFormValid
            ) {
                Text("저장하기")
            }
        }
        is PasswordFormState.ReadOnly -> {
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
                ) {
                    Text("삭제하기")
                }

                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("수정하기")
                }
            }
        }
        is PasswordFormState.Update -> {
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 48.dp),
                enabled = isFormValid
            ) {
                Text("수정하기")
            }
        }
    }
}
