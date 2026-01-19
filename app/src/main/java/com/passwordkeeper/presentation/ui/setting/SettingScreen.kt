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
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
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
        val context = LocalContext.current
        val versionName = remember {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }
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
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://docs.google.com/forms/d/e/1FAIpQLSeb-fVpdF_J8iHZwGyPNuEgqhrXNwKkZ6L36MgLUr2IwLcMHg/viewform"
                        )
                    )
                    context.startActivity(intent)
                }
            )
            SettingItem(
                modifier = Modifier,
                title = "개인정보 처리방침",
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://pointy-hourglass-08a.notion.site/2edcf38172a8804aa423f912f2128660?source=copy_link"
                        )
                    )
                    context.startActivity(intent)
                }
            )
            SettingItem(
                modifier = Modifier,
                title = "버전 정보",
                description = "v$versionName",
                onClick = {}
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
