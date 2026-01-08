package com.passwordkeeper.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // T1 Medium 16pt
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    // T1 SemiBold 32pt
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    ),
    // T1 Bold 20pt
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),

    //////////////////////////////////////

    // T2 SemiBold 24pt
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    // T2 Bold 18pt
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    // T2 Medium 15pt
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    ),

    ////////////////////////////////////////

    // T3 SemiBold 20pt
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    // T4 SemiBold 16pt
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
)

val Typography.t1Medium16: TextStyle
    get() = bodyLarge

val Typography.t1SemiBold32: TextStyle
    get() = displayLarge

val Typography.t1Bold20: TextStyle
    get() = displayMedium

val Typography.t2SemiBold24: TextStyle
    get() = headlineLarge

val Typography.t2Bold18: TextStyle
    get() = headlineMedium

val Typography.t2Medium15: TextStyle
    get() = headlineSmall

val Typography.t3SemiBold20: TextStyle
    get() = titleLarge

val Typography.t4SemiBold16: TextStyle
    get() = titleMedium
