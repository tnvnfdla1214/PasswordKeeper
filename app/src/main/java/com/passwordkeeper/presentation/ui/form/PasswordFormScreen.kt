package com.passwordkeeper.presentation.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.presentation.viewmodel.PasswordFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFormScreen(
    viewModel: PasswordFormViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val serviceName by viewModel.serviceName.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val password by viewModel.password.collectAsState()
    val memo by viewModel.memo.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "비밀번호 수정" else "비밀번호 추가") },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = serviceName,
                onValueChange = { viewModel.onServiceNameChange(it) },
                label = { Text("서비스명") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userId,
                onValueChange = { viewModel.onUserIdChange(it) },
                label = { Text("아이디") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("비밀번호") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = memo,
                onValueChange = { viewModel.onMemoChange(it) },
                label = { Text("메모 (선택)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.savePassword(onSaveSuccess) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving && serviceName.isNotBlank() && userId.isNotBlank() && password.isNotBlank()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("저장")
                }
            }
        }
    }
}
