package com.passwordkeeper.presentation.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.passwordkeeper.presentation.ui.auth.AuthScreen
import com.passwordkeeper.presentation.ui.auth.ResetPasswordScreen
import com.passwordkeeper.presentation.ui.form.PasswordFormScreen
import com.passwordkeeper.presentation.ui.home.HomeScreen
import com.passwordkeeper.presentation.ui.onboarding.OnboardingScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Auth.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onStartClick = {
                    navController.navigate(Screen.ResetPassword.route)
                }
            )
        }

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
                    navController.popBackStack()
                },
                onResetSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            val activity = LocalContext.current as? Activity

            BackHandler {
                activity?.finishAffinity()
            }

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
                navArgument(NavArgs.PASSWORD_ID) {
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
