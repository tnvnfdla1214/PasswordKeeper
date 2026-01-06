package com.passwordkeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.passwordkeeper.presentation.ui.auth.AuthScreen
import com.passwordkeeper.presentation.ui.auth.ResetPasswordScreen
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
                },
                onResetPassword = {
                    navController.navigate(Screen.ResetPassword.route)
                }
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onBackClick = {
                    if (startDestination == Screen.ResetPassword.route) {
                        // 첫 시작이 ResetPassword인 경우 뒤로가기 차단
                    } else {
                        navController.popBackStack()
                    }
                },
                onResetSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.ResetPassword.route) { inclusive = true }
                    }
//                    if (startDestination == Screen.ResetPassword.route) {
//                        // 첫 시작이 ResetPassword인 경우 AuthScreen으로 이동
//                        navController.navigate(Screen.Auth.route) {
//                            popUpTo(Screen.ResetPassword.route) { inclusive = true }
//                        }
//                    } else {
//                        navController.popBackStack()
//                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onItemClick = { passwordId ->
                    navController.navigate(Screen.Form.createRoute(passwordId))
                },
                onAddClick = {
                    navController.navigate(Screen.Form.createRoute())
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
            )
        }
    }
}
