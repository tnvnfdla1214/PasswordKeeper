package com.passwordkeeper.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 화살표가 있는 말풍선 컴포넌트
 *
 * @param text 표시할 텍스트
 * @param backgroundColor 배경색 (기본값: 파란색)
 * @param textColor 텍스트 색 (기본값: 흰색)
 * @param cornerRadius 모서리 라운드 정도
 * @param arrowHeight 화살표 높이
 * @param arrowWidth 화살표 너비
 */
@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF355F9B),
    textColor: Color = Color.White,
    arrowHeight: Float = 12f,
    arrowWidth: Float = 14f
) {
    Column {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(arrowWidth.dp)
                .height(arrowHeight.dp)
                .drawBehind {
                    val path = Path().apply {
                        moveTo(arrowWidth.dp.toPx() / 2, 0f)
                        lineTo(0f, arrowHeight.dp.toPx())
                        lineTo(arrowWidth.dp.toPx(), arrowHeight.dp.toPx())
                        close()
                    }
                    drawPath(path, backgroundColor)
                }
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .background(backgroundColor)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeight = 24.sp)
            )
        }
    }
}

/**
 * 하단에 화살표가 있는 말풍선 (반대 방향)
 */
@Composable
fun SpeechBubbleBottom(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF355F9B),
    textColor: Color = Color.White,
    cornerRadius: Int = 10,
    arrowHeight: Float = 12f,
    arrowWidth: Float = 14f
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 말풍선 본체
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius.dp))
                .background(backgroundColor)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeight = 24.sp)
            )
        }

        // 화살표 (하단)
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(arrowWidth.dp)
                .height(arrowHeight.dp)
                .drawBehind {
                    val path = Path().apply {
                        moveTo(0f, 0f)  // 왼쪽 위
                        lineTo(arrowWidth.dp.toPx(), 0f)
                        lineTo(arrowWidth.dp.toPx() / 2, arrowHeight.dp.toPx())
                        close()
                    }
                    drawPath(path, backgroundColor)
                }
        )
    }
}




