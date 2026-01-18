package com.passwordkeeper.presentation.ui.form

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.util.InterstitialAdManager
import com.passwordkeeper.presentation.ui.components.CustomDialog
import com.passwordkeeper.presentation.ui.components.DialogType
import com.passwordkeeper.presentation.ui.theme.Neutral100
import com.passwordkeeper.presentation.ui.theme.Neutral50
import com.passwordkeeper.presentation.ui.theme.Primary600
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

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val adManager = remember { InterstitialAdManager() }

    // 화면 진입 시 광고 미리 로드
    LaunchedEffect(Unit) {
        adManager.loadAd(context)
    }

    var savedState by remember { mutableStateOf<PasswordFormState?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0

    if (savedState != null) {
        CustomDialog(
            type = DialogType.CONFIRM,
            title = when (savedState) {
                is PasswordFormState.Register -> "내 정보가 잘 저장 되었어요"
                is PasswordFormState.Update -> "내 정보가 잘 수정 되었어요"
                else -> "완료되었어요"
            },
            onDismissRequest = { savedState = null }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
    ) {
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
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "서비스명",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = serviceName,
                onValueChange = { viewModel.onServiceNameChange(it) },
                label = { },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = formState is PasswordFormState.ReadOnly,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Neutral50,
                    unfocusedContainerColor = Neutral50,
                    disabledContainerColor = Neutral50,
                    focusedBorderColor = Primary600,
                    unfocusedBorderColor = Neutral100,
                    disabledBorderColor = Neutral100
                ),
            )

            Text(
                text = "아이디",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 21.dp, bottom = 4.dp),
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
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Neutral50,
                    unfocusedContainerColor = Neutral50,
                    disabledContainerColor = Neutral50,
                    focusedBorderColor = Primary600,
                    unfocusedBorderColor = Neutral100,
                    disabledBorderColor = Neutral100
                ),
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
                modifier = Modifier.padding(top = 21.dp, bottom = 4.dp),
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
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Neutral50,
                    unfocusedContainerColor = Neutral50,
                    disabledContainerColor = Neutral50,
                    focusedBorderColor = Primary600,
                    unfocusedBorderColor = Neutral100,
                    disabledBorderColor = Neutral100
                ),
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
                modifier = Modifier.padding(top = 21.dp, bottom = 4.dp),
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
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Neutral50,
                    unfocusedContainerColor = Neutral50,
                    disabledContainerColor = Neutral50,
                    focusedBorderColor = Primary600,
                    unfocusedBorderColor = Neutral100,
                    disabledBorderColor = Neutral100
                ),
            )
        }

        PasswordFormButtons(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
            formState = formState,
            serviceName = serviceName,
            userId = userId,
            password = password,
            memo = memo,
            onDeleteClick = { showDeleteDialog = true },
            onSaveClick = {
                viewModel.savePassword(onSuccess = { state, shouldShowAd ->
                    savedState = state
                    if (shouldShowAd) {
                        val activity = context as? Activity
                        if (activity != null) {
                            adManager.showAd(activity) {}
                        }
                    }
                })
            },
            onEditClick = {
                viewModel.switchToUpdateMode()
            }
        )

        val spacerHeight by animateDpAsState(
            targetValue = if (isKeyboardVisible) 0.dp else 44.dp,
            label = "spacerHeight"
        )
        Spacer(modifier = Modifier.height(spacerHeight))
    }
}

@Composable
private fun PasswordFormButtons(
    modifier: Modifier,
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
                modifier = modifier,
                onClick = onSaveClick,
                enabled = isFormValid
            ) {
                Text("저장하기")
            }
        }
        is PasswordFormState.ReadOnly -> {
            Row(
                modifier = modifier,
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
                modifier = modifier,
                enabled = isFormValid
            ) {
                Text("수정하기")
            }
        }
    }
}
