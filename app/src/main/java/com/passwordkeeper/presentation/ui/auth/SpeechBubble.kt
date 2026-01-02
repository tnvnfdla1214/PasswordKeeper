package com.passwordkeeper.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.IntOffset

@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF355F9B),
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(14f.dp)
                .height(12f.dp)
                .offset(y = 1.dp)
                .drawBehind {
                    val path = Path().apply {
                        moveTo(14f.dp.toPx() / 2, 0f)
                        lineTo(0f, 12f.dp.toPx())
                        lineTo(14f.dp.toPx(), 12f.dp.toPx())
                        close()
                    }
                    drawPath(path, backgroundColor)
                }
        )

        Box(
            modifier = Modifier
                .offset(y = (-1).dp)
                .clip(RoundedCornerShape(10))
                .background(backgroundColor)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeight = 24.sp)
            )
        }
    }
}
