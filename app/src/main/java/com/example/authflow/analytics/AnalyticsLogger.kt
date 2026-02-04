package com.example.authflow.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class AnalyticsLogger(
    private val analytics: FirebaseAnalytics
) {

    fun otpGenerated(email: String) {
        analytics.logEvent("otp_generated") {
            param("email", email)
        }
    }

    fun otpResent(email: String) {
        analytics.logEvent("otp_resent") {
            param("email", email)
        }
    }

    fun otpSuccess() {
        analytics.logEvent("otp_validation_success") {
            param("status", "success")
        }
    }

    fun otpFailure(reason: String) {
        analytics.logEvent("otp_validation_failure") {
            param("reason", reason)
        }
    }

    fun logout() {
        analytics.logEvent("logout") {
            param("action", "user_logout")
        }
    }
}
