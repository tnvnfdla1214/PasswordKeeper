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
                    singleLine = true
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
                    singleLine = true
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
                    singleLine = true
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
                    maxLines = 5
                )
            }

            Button(
                onClick = { viewModel.savePassword(onSaveSuccess) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 48.dp),
                enabled = !isSaving && serviceName.isNotBlank() && userId.isNotBlank() && password.isNotBlank()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("저장하기")
                }
            }
        }
    }
}
