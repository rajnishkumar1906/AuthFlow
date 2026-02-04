package com.example.authflow.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.authflow.viewmodel.AuthViewModel

@Composable
fun SessionScreen(
    vm: AuthViewModel,
    sessionStart: Long
) {
    var elapsedSeconds by remember { mutableStateOf(0L) }

    LaunchedEffect(sessionStart) {
        while (true) {
            elapsedSeconds = (System.currentTimeMillis() - sessionStart) / 1000
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Session Active",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Duration: ${elapsedSeconds / 60}:${elapsedSeconds % 60}"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { vm.logout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
