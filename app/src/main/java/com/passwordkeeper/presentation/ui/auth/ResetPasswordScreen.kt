package com.passwordkeeper.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.R
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
    val oldPassword by viewModel.oldPassword
    val newPassword by viewModel.newPassword
    val confirmPassword by viewModel.confirmPassword
    val showStep by viewModel.showStep
    val showSuccessDialog by viewModel.showSuccessDialog
    val showErrorDialog by viewModel.showErrorDialog
    val errorMessage by viewModel.errorMessage

    if (showSuccessDialog) {
        CustomDialog(
            type = DialogType.CONFIRM,
            title = "비밀번호가 설정되었습니다",
            onDismissRequest = {
                viewModel.dismissSuccessDialog()
                onResetSuccess()
            },
        )
    }

    if (showErrorDialog) {
        CustomDialog(
            type = DialogType.WARNING,
            title = errorMessage,
            onDismissRequest = { viewModel.dismissErrorDialog() },
            button2Text = "확인",
            button2Action = { viewModel.dismissErrorDialog() }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(top = 64.dp),
                title = {
                    Text(
                        text = when (showStep) {
                            0 -> "기존 비밀번호를 눌러 주세요"
                            1 -> "새로운 비밀번호를 눌러 주세요"
                            else -> "비밀번호를 다시 한번\n눌러 주세요"
                        },
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_screen_backstack),
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
        ) {

            Column {
                PasswordIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    passwordLength = when (showStep) {
                        0 -> oldPassword.length
                        1 -> newPassword.length
                        else -> confirmPassword.length
                    },
                )

                Spacer(Modifier.height(40.dp))

                PasswordKeypad(
                    modifier = Modifier.fillMaxWidth(),
                    onNumberClick = { number ->
                        when (showStep) {
                            0 -> viewModel.onOldPasswordInput(number.toString())
                            1 -> viewModel.onNewPasswordInput(number.toString())
                            2 -> viewModel.onConfirmPasswordInput(number.toString())
                        }
                    },
                    onDeleteClick = {
                        viewModel.onDeleteClick()
                    },
                )
            }

            Spacer(modifier = Modifier.weight(0.7f))
        }
    }
}
