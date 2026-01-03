package com.passwordkeeper.presentation.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object ResetPassword : Screen("resetPassword")
    data object Home : Screen("home")
    data object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Long) = "detail/$itemId"
    }
    data object Form : Screen("form?itemId={itemId}") {
        fun createRoute(itemId: Long? = null) =
            if (itemId != null) "form?itemId=$itemId"
            else "form"
    }
}
