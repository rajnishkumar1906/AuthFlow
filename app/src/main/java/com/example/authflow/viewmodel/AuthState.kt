package com.example.authflow.viewmodel

data class AuthState(
    val email: String = "",
    val otpSent: Boolean = false,
    val loggedIn: Boolean = false,
    val sessionStart: Long? = null,
    val remainingMs: Long = 60_000L,
    val attemptsLeft: Int = 3,
    val error: String? = null
)
