package com.passwordkeeper.presentation.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordKeypad(
    onNumberClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    onBiometricClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        KeypadRow(
            numbers = listOf(1, 2, 3),
            onNumberClick = onNumberClick
        )

        KeypadRow(
            numbers = listOf(4, 5, 6),
            onNumberClick = onNumberClick
        )

        KeypadRow(
            numbers = listOf(7, 8, 9),
            onNumberClick = onNumberClick
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onBiometricClick() }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = com.passwordkeeper.R.drawable.fingerprint),
                    contentDescription = "지문 인식",
                    modifier = Modifier.size(49.dp),
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onNumberClick(0) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 13.dp),
                    text = "0",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onDeleteClick() }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = com.passwordkeeper.R.drawable.backspace),
                    contentDescription = "삭제",
                    modifier = Modifier.size(40.dp),
                )
            }
        }
    }
}

@Composable
private fun KeypadRow(
    numbers: List<Int>,
    onNumberClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        numbers.forEach { number ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onNumberClick(number) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 13.dp),
                    text = number.toString(),
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
