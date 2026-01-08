package com.passwordkeeper.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    // Primary Colors
    primary = Primary600,
    onPrimary = Color.White,
    primaryContainer = Primary100,
    onPrimaryContainer = Primary900,

    // Secondary Colors
    secondary = Primary400,
    onSecondary = Color.White,
    secondaryContainer = Primary200,
    onSecondaryContainer = Primary800,

    // Tertiary Colors
    tertiary = Primary500,
    onTertiary = Color.White,
    tertiaryContainer = Primary100,
    onTertiaryContainer = Primary900,

    // Error Colors
    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFDE7E9),
    onErrorContainer = Color(0xFF410002),

    // Background & Surface
    background = Color.White,
    onBackground = Neutral900,
    surface = Color.White,
    onSurface = Neutral900,
    surfaceVariant = Neutral50,
    onSurfaceVariant = Neutral600,

    // Outline
    outline = Neutral300,
    outlineVariant = Neutral100
)

@Composable
fun PasswordKeeperTheme(
    darkTheme: Boolean = false, // 다크 테마 비활성화
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
