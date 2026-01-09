package com.passwordkeeper.presentation.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.passwordkeeper.R
import com.passwordkeeper.presentation.ui.theme.Neutral50
import com.passwordkeeper.presentation.ui.theme.Primary600
import com.passwordkeeper.presentation.viewmodel.PasswordFormState
import com.passwordkeeper.presentation.viewmodel.PasswordFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("설정")
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)  // 아이템 간 간격
        ) {
            SettingItem(
                modifier = Modifier,
                title = "문의사항",
                onClick = {
                    //클릭
                }
            )
            SettingItem(
                modifier = Modifier,
                title = "이용약관",
                onClick = {
                    //클릭
                }
            )
            SettingItem(
                modifier = Modifier,
                title = "사용자 개인정보 선택 사항 안내",
                onClick = {
                    //클릭
                }
            )
            SettingItem(
                modifier = Modifier,
                title = "개인정보 처리방침",
                onClick = {
                    //클릭
                }
            )
            SettingItem(
                modifier = Modifier,
                title = "버전 정보",
                description = "v1.0.0", //todo : 버전 가져와야 함
                onClick = {
                    //클릭
                }
            )
            SettingItem(
                modifier = Modifier,
                title = "탈퇴하기",
                onClick = {
                    //클릭
                }
            )

        }
    }
}

@Composable
private fun SettingItem(
    modifier: Modifier,
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .background(Neutral50)
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        if (description != null) {
            Text(
                text = description,
                fontSize = 16.sp,
                color = Primary600
            )
        } else {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.left_arrow),
                contentDescription = "왼쪽 화살표 이미지",
            )
        }
    }
}
