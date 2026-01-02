package com.passwordkeeper.presentation.ui.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.presentation.viewmodel.AuthState
import com.passwordkeeper.presentation.viewmodel.AuthViewModel
import com.passwordkeeper.util.BiometricAuthManager

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    val biometricAuthManager = remember { BiometricAuthManager() }

    val handleBiometricAuth: () -> Unit = {
        activity?.let { fragmentActivity ->
            biometricAuthManager.authenticate(
                activity = fragmentActivity,
                onSuccess = {
                    onAuthSuccess()
                },
                onError = { _ -> },
                onFailed = {}
            )
        }
    }

    // 화면 진입 시 자동으로 생체인식 실행
    LaunchedEffect(Unit) {
        handleBiometricAuth()
    }

    LaunchedEffect(password) {
        if (password.length == 4) {
            viewModel.validatePassword(password)
        }
    }

    LaunchedEffect(authState) {
        Log.d("qweqwe","$authState")
        when (authState) {
            is AuthState.Success -> {
                onAuthSuccess()
                password = ""
                viewModel.resetAuthState()
            }
            is AuthState.Error -> {
                password = ""
                viewModel.setAuthErrorIdle()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        // 상단 영역
        Text(
            text = when (authState) {
                is AuthState.Error,AuthState.ErrorIdle -> "다시 한번 눌러 주세요"
                else -> "비밀번호를 놀러 주세요"
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
        )

        // ========== 중앙 콘텐츠 영역 ==========
        Column {
            PasswordIndicator(
                modifier = modifier.fillMaxWidth(),
                passwordLength = password.length,
            )
            Spacer(Modifier.height(32.dp))
            PasswordKeypad(
                modifier = Modifier.fillMaxWidth(),
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
            )
            Spacer(Modifier.height(32.dp))
            Image(
                painter = painterResource(id = com.passwordkeeper.R.drawable.fingerprint),
                contentDescription = "지문 인식",
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { handleBiometricAuth() }
            )

            SpeechBubble(
                text = "지문으로도 로그인할 수 있어요",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.heightIn(min = 65.dp))
        }


    }
}

@Composable
fun PasswordIndicator(
    modifier: Modifier = Modifier,
    passwordLength: Int,
    passwordMaxSize: Int = 4,
) {
    Row(
        modifier = modifier,
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
            }
            if (index < 3) {
                Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }
}

