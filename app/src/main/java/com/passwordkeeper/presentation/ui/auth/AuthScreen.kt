package com.passwordkeeper.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.passwordkeeper.presentation.ui.components.SpeechBubble

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    onBiometricAuth: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }

    LaunchedEffect(password) {
        if (password.length == 4) {
            // TODO: 실제로는 비밀번호 검증 후 성공 시 호출
            onAuthSuccess()
            password = ""
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            //todo : 상황에 따라 text가 변경되는 기능을 추가해야 한다
            Text(
                text = "비밀번호를 놀러 주세요",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            PasswordIndicator(
                passwordLength = password.length,
                modifier = Modifier.padding(bottom = 23.dp),
            )

            //todo : 상황에 따라 text가 변경되는 기능을 추가해야 한다
            Text(
                text = "재대로 입력하신게 맞나요?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 56.dp),
            )

            PasswordKeypad(
                onNumberClick = { number ->
                    if (password.length < 4) {
                        password += number.toString()
                    }
                },
                onDeleteClick = {
                    if (password.isNotEmpty()) {
                        password = password.dropLast(1)
                    }
                },
                onBiometricClick = onBiometricAuth,
                modifier = Modifier
                    .fillMaxWidth()
            )

            SpeechBubble(
                text = "지문으로도 로그인할 수 있어요",
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}

@Composable
fun PasswordIndicator(
    passwordLength: Int,
    passwordMaxSize: Int = 4,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(passwordMaxSize) { index ->
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        //Todo : 입력을 받을 시 어떻게 변경될지는 아직 안정함
                        if (index < passwordLength) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 필요시 내부에 표시할 내용 추가 가능
            }
            if (index < 3) {
                Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }
}

