package com.example.authflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authflow.analytics.AnalyticsLogger
import com.example.authflow.data.OtpManager
import com.example.authflow.data.OtpResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val otpManager: OtpManager,
    private val analytics: AnalyticsLogger
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private var countdownJob: Job? = null

    fun sendOtp(email: String): String {
        val otp = otpManager.generate(email)
        analytics.otpGenerated()

        _state.value = AuthState(
            email = email,
            otpSent = true,
            remainingMs = 60_000L,
            attemptsLeft = 3
        )

        startCountdown()
        return otp
    }

    fun verifyOtp(input: String) {
        val email = _state.value.email

        when (val result = otpManager.validate(email, input)) {

            is OtpResult.Success -> {
                analytics.otpSuccess()
                countdownJob?.cancel()

                _state.value = _state.value.copy(
                    loggedIn = true,
                    sessionStart = System.currentTimeMillis()
                )
            }

            is OtpResult.Invalid -> {
                analytics.otpFailure("invalid_otp")
                _state.value = _state.value.copy(
                    attemptsLeft = result.attemptsLeft,
                    error = "Invalid OTP. Attempts left: ${result.attemptsLeft}"
                )
            }

            is OtpResult.Expired -> {
                analytics.otpFailure("expired_otp")
                _state.value = _state.value.copy(
                    remainingMs = 0,
                    error = "OTP expired"
                )
            }

            is OtpResult.TooManyAttempts -> {
                analytics.otpFailure("too_many_attempts")
                _state.value = _state.value.copy(
                    error = "Too many attempts. Please resend OTP."
                )
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch {
            while (_state.value.remainingMs > 0) {
                delay(1000)
                _state.value = _state.value.copy(
                    remainingMs = _state.value.remainingMs - 1000
                )
            }
        }
    }

    fun resendOtp() {
        val email = _state.value.email
        if (email.isBlank()) return

        otpManager.generate(email)
        analytics.otpResent()   // ✅ CORRECT EVENT

        _state.value = _state.value.copy(
            remainingMs = 60_000L,
            attemptsLeft = 3,
            error = null
        )

        startCountdown()
    }

    fun logout() {
        analytics.logout()
        countdownJob?.cancel()
        _state.value = AuthState()
    }
}
