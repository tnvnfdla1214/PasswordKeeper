package com.passwordkeeper.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.presentation.ui.components.CustomDialog
import com.passwordkeeper.presentation.ui.components.DialogType
import com.passwordkeeper.presentation.viewmodel.ResetPasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    onBackClick: () -> Unit,
    onResetSuccess: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showStep by remember { mutableIntStateOf(1) } // 1: 새 비밀번호, 2: 확인 비밀번호
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(newPassword) {
        if (newPassword.length == 4) {
            showStep = 2
        }
    }

    LaunchedEffect(confirmPassword) {
        if (confirmPassword.length < 4) return@LaunchedEffect

        if (newPassword != confirmPassword) {
            errorMessage = "비밀번호가 일치하지 않습니다"
            showErrorDialog = true
            confirmPassword = ""
            return@LaunchedEffect
        }

        viewModel.savePassword(newPassword) { success ->
            if (success) {
                showSuccessDialog = true
            } else {
                errorMessage = "비밀번호 저장에 실패했습니다"
                showErrorDialog = true
                confirmPassword = ""
            }
        }
    }

    if (showSuccessDialog) {
        CustomDialog(
            type = DialogType.CONFIRM,
            title = "비밀번호가 변경되었습니다",
            onDismissRequest = {
                showSuccessDialog = false
                onResetSuccess()
            },
        )
    }

    if (showErrorDialog) {
        CustomDialog(
            type = DialogType.WARNING,
            title = errorMessage,
            onDismissRequest = { showErrorDialog = false },
            button2Text = "확인",
            button2Action = { showErrorDialog = false }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
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
                .padding(horizontal = 16.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = if (showStep == 1) {
                        "새로운 비밀번호를 눌러 주세요"
                    } else {
                        "비밀번호를 다시 한번\n눌러 주세요"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Column {
                PasswordIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    passwordLength = if (showStep == 1) newPassword.length else confirmPassword.length,
                )

                Spacer(Modifier.height(40.dp))

                PasswordKeypad(
                    modifier = Modifier.fillMaxWidth(),
                    onNumberClick = { number ->
                        if (showStep == 1 && newPassword.length < 4) {
                            newPassword += number.toString()
                        } else if (showStep == 2 && confirmPassword.length < 4) {
                            confirmPassword += number.toString()
                        }
                    },
                    onDeleteClick = {
                        if (showStep == 1 && newPassword.isNotEmpty()) {
                            newPassword = newPassword.dropLast(1)
                        } else if (showStep == 2 && confirmPassword.isNotEmpty()) {
                            confirmPassword = confirmPassword.dropLast(1)
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.weight(0.7f))
        }
    }
}
