package com.passwordkeeper.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.passwordkeeper.R

val NotoSansKR = FontFamily(
    Font(R.font.noto_sans_kr_medium, FontWeight.W500),
    Font(R.font.noto_sans_kr_semibold, FontWeight.W600),
    Font(R.font.noto_sans_kr_bold, FontWeight.Bold)
)

val Typography = Typography(
    // T1 - Semi Bold 32sp
    displayLarge = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp,
        letterSpacing = 0.64.sp  // 32 * 2% = 0.64sp
    ),

    // T1 - Bold 20sp
    displayMedium = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 0.4.sp  // 20 * 2% = 0.4sp
    ),

    // T1 - Medium 16sp
    displaySmall = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        letterSpacing = 0.32.sp  // 16 * 2% = 0.32sp
    ),

    // T2 - Semi Bold 24sp
    headlineLarge = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        letterSpacing = 0.48.sp  // 24 * 2% = 0.48sp
    ),

    // T2 - Bold 18sp
    headlineMedium = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 0.36.sp  // 18 * 2% = 0.36sp
    ),

    // T2 - Medium 15sp
    headlineSmall = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.W500,
        fontSize = 15.sp,
        letterSpacing = 0.3.sp  // 15 * 2% = 0.3sp
    ),

    // T3 - Semi Bold 20sp
    titleLarge = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        letterSpacing = 0.4.sp  // 20 * 2% = 0.4sp
    ),

    // T4 - Semi Bold 16sp
    titleMedium = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        letterSpacing = 0.32.sp  // 16 * 2% = 0.32sp
    )
)

// 확장 속성 - 더 명확한 이름으로 사용

// T1
val Typography.t1Medium: TextStyle
    get() = displaySmall

val Typography.t1SemiBold: TextStyle
    get() = displayLarge

val Typography.t1Bold: TextStyle
    get() = displayMedium

// T2
val Typography.t2Medium: TextStyle
    get() = headlineSmall

val Typography.t2SemiBold: TextStyle
    get() = headlineLarge

val Typography.t2Bold: TextStyle
    get() = headlineMedium

// T3
val Typography.t3SemiBold: TextStyle
    get() = titleLarge

// T4
val Typography.t4SemiBold: TextStyle
    get() = titleMedium
