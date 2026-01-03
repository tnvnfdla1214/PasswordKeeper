package com.passwordkeeper

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.passwordkeeper.domain.usecase.HasMasterPasswordUseCase
import com.passwordkeeper.presentation.navigation.NavGraph
import com.passwordkeeper.presentation.navigation.Screen
import com.passwordkeeper.presentation.ui.theme.PasswordKeeperTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var hasMasterPasswordUseCase: HasMasterPasswordUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // AdMob 초기화
        MobileAds.initialize(this) {}

        setContent {
            var startDestination by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    val hasMasterPassword = hasMasterPasswordUseCase()
                    startDestination = if (hasMasterPassword) {
                        Screen.Auth.route
                    } else {
                        Screen.ResetPassword.route
                    }
                }
            }

            PasswordKeeperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    startDestination?.let { destination ->
                        val navController = rememberNavController()
                        NavGraph(
                            navController = navController,
                            startDestination = destination
                        )
                    }
                }
            }
        }
    }
}
