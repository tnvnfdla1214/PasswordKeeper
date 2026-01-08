package com.passwordkeeper.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.passwordkeeper.R

enum class DialogType(val iconRes: Int) {
    WARNING(R.drawable.worning),
    CONFIRM(R.drawable.confirm)
}

@Composable
fun CustomDialog(
    title: String,
    type: DialogType = DialogType.WARNING,
    onDismissRequest: () -> Unit,
    button1Text: String? = null,
    button1Action: (() -> Unit)? = null,
    button2Text: String? = null,
    button2Action: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = type.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).padding(bottom = 4.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                if (button1Text != null || button2Text != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (button1Text != null && button1Action != null) {
                            Button(
                                onClick = button1Action,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD9D9D9)
                                )
                            ) {
                                Text(
                                    text = button1Text,
                                    color = Color.Black
                                )
                            }
                        }

                        if (button2Text != null && button2Action != null) {
                            Button(
                                onClick = button2Action,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF355F9B)
                                )
                            ) {
                                Text(button2Text)
                            }
                        }
                    }
                }
            }
        }
    }
}
