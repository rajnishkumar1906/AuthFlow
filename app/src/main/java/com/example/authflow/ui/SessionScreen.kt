package com.example.authflow.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.authflow.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SessionScreen(
    vm: AuthViewModel,
    sessionStart: Long,
    email: String
) {
    var elapsedSeconds by remember { mutableStateOf(0L) }

    LaunchedEffect(sessionStart) {
        while (true) {
            elapsedSeconds = (System.currentTimeMillis() - sessionStart) / 1000
            delay(1000)
        }
    }

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val formattedDuration = String.format("%02d:%02d", minutes, seconds)

    val startTimeFormatted = remember(sessionStart) {
        SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
            .format(Date(sessionStart))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Session Active",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = formattedDuration,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Started at $startTimeFormatted",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = { vm.logout() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Logout",
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}
