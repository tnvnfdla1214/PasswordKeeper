package com.passwordkeeper.presentation.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object ResetPassword : Screen("resetPassword")
    data object Home : Screen("home")
    data object Form : Screen("form?${NavArgs.PASSWORD_ID}={${NavArgs.PASSWORD_ID}}") {
        fun createRoute(passwordId: Long? = null) =
            if (passwordId != null) "form?${NavArgs.PASSWORD_ID}=$passwordId"
            else "form"
    }
}
