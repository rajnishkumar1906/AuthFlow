package com.example.authflow

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.authflow.analytics.AnalyticsLogger
import com.example.authflow.data.OtpManager
import com.example.authflow.ui.*
import com.example.authflow.viewmodel.AuthViewModel
import com.example.authflow.ui.theme.AuthFlowTheme
import com.google.firebase.analytics.FirebaseAnalytics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.*
class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔔 Request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        val vm = AuthViewModel(
            otpManager = OtpManager(),
            analytics = AnalyticsLogger(
                FirebaseAnalytics.getInstance(this)
            )
        )

        setContent {
            AuthFlowTheme {

                var showSplash by remember { mutableStateOf(true) }

                val state by vm.state.collectAsState()
                val sessionStart = state.sessionStart

                when {
                    showSplash -> {
                        SplashScreen {
                            showSplash = false
                        }
                    }

                    state.loggedIn && sessionStart != null -> {
                        SessionScreen(vm, sessionStart)
                    }

                    state.otpSent -> {
                        OtpScreen(vm, state)
                    }

                    else -> {
                        LoginScreen(vm)
                    }
                }
            }
        }

    }
}
