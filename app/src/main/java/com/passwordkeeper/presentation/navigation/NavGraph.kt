package com.passwordkeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.passwordkeeper.presentation.ui.auth.AuthScreen
import com.passwordkeeper.presentation.ui.detail.DetailScreen
import com.passwordkeeper.presentation.ui.form.PasswordFormScreen
import com.passwordkeeper.presentation.ui.home.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Auth.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onPasswordClick = { passwordId ->
                    navController.navigate(Screen.Detail.createRoute(passwordId))
                },
                onAddClick = {
                    navController.navigate(Screen.Form.createRoute())
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("passwordId") { type = NavType.LongType }
            )
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() },
                onEditClick = { passwordId ->
                    navController.navigate(Screen.Form.createRoute(passwordId))
                }
            )
        }

        composable(
            route = Screen.Form.route,
            arguments = listOf(
                navArgument("passwordId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            PasswordFormScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}
