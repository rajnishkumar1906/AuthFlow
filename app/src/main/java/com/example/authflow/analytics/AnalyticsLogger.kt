package com.example.authflow.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsLogger(private val analytics: FirebaseAnalytics) {

    fun otpGenerated() {
        analytics.logEvent("otp_generated", null)
    }

    fun otpResent() {
        analytics.logEvent("otp_resent", null)
    }

    fun otpSuccess() {
        analytics.logEvent("otp_validation_success", null)
    }

    fun otpFailure(reason: String) {
        val bundle = Bundle().apply {
            putString("reason", reason)
        }
        analytics.logEvent("otp_validation_failure", bundle)
    }

    fun logout() {
        analytics.logEvent("logout", null)
    }
}
