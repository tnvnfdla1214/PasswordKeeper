package com.passwordkeeper.presentation.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object ResetPassword : Screen("resetPassword")
    data object Home : Screen("home")
    data object Form : Screen("form?passwordId={passwordId}") {
        fun createRoute(passwordId: Long? = null) =
            if (passwordId != null) "form?passwordId=$passwordId"
            else "form"
    }
}
