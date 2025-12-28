package com.passwordkeeper.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    var showPasswordInput by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "비밀번호 저장소",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(48.dp))

            if (!showPasswordInput) {
                // 지문 인식 버튼
                FilledTonalButton(
                    onClick = { /* 지문 인식 로직 */ },
                    modifier = Modifier.size(120.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "지문 인식",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("지문 인식")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = { showPasswordInput = true }) {
                    Text("비밀번호로 입력")
                }
            } else {
                // 비밀번호 입력
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("마스터 비밀번호") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // 비밀번호 검증 로직
                        onAuthSuccess()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("확인")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { showPasswordInput = false }) {
                    Text("지문 인식으로 돌아가기")
                }
            }
        }
    }
}
