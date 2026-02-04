package com.example.authflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authflow.analytics.AnalyticsLogger
import com.example.authflow.data.OtpManager
import com.example.authflow.data.OtpResult
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
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val newTime = _state.value.remainingMs - 1000
                if (newTime <= 0) {
                    _state.value = _state.value.copy(remainingMs = 0)
                    break
                } else {
                    _state.value = _state.value.copy(remainingMs = newTime)
                }
            }
        }
    }

    fun logout() {
        analytics.logout()
        _state.value = AuthState()
    }
}
