package com.passwordkeeper.presentation.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Long) = "detail/$itemId"
    }
    object Form : Screen("form?itemId={itemId}&type={type}") {
        fun createRoute(itemId: Long? = null, type: String? = null) =
            if (itemId != null) "form?itemId=$itemId"
            else if (type != null) "form?type=$type"
            else "form"
    }
}
