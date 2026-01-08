package com.passwordkeeper.presentation.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    onStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 36.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF355F9B),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("지문")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("으로 ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF355F9B),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("안전")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("하게\n열리는 비밀번호 금고")
                    }
                },
                lineHeight = 48.sp
            )
        }

        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF355F9B)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "비밀번호 설정하러 가기",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
