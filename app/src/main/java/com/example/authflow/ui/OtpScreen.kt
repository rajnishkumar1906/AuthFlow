package com.example.authflow.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.authflow.viewmodel.AuthState
import com.example.authflow.viewmodel.AuthViewModel

@Composable
fun OtpScreen(
    vm: AuthViewModel,
    state: AuthState
) {
    var otp by rememberSaveable { mutableStateOf("") }
    val secondsLeft = state.remainingMs / 1000

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Enter OTP", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Time left: ${secondsLeft}s",
            color = if (secondsLeft > 0)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.error
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Attempts left: ${state.attemptsLeft}",
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("6-digit OTP") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { vm.verifyOtp(otp) },
            enabled = secondsLeft > 0 && state.attemptsLeft > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify OTP")
        }

        state.error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
