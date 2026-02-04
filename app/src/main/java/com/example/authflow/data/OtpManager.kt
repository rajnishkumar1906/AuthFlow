package com.example.authflow.data

import android.util.Log
import kotlin.random.Random

data class OtpData(
    val otp: String,
    val createdAt: Long,
    var attemptsLeft: Int
)

class OtpManager {

    private val store = mutableMapOf<String, OtpData>()
    private val EXPIRY_MS = 60_000L

    fun generate(email: String): String {
        val otp = Random.nextInt(100000, 999999).toString()
        store[email] = OtpData(otp, System.currentTimeMillis(), 3)

        Log.d("OTP_DEBUG", "OTP for $email = $otp")
        return otp
    }

    fun validate(email: String, input: String): OtpResult {
        val data = store[email] ?: return OtpResult.Expired

        if (System.currentTimeMillis() - data.createdAt > EXPIRY_MS) {
            store.remove(email)
            return OtpResult.Expired
        }

        if (data.attemptsLeft <= 0) {
            return OtpResult.TooManyAttempts
        }

        return if (data.otp == input) {
            store.remove(email)
            OtpResult.Success
        } else {
            data.attemptsLeft--
            OtpResult.Invalid(data.attemptsLeft)
        }
    }

    fun remainingTime(email: String): Long {
        val data = store[email] ?: return 0
        val elapsed = System.currentTimeMillis() - data.createdAt
        return (EXPIRY_MS - elapsed).coerceAtLeast(0)
    }
}

sealed class OtpResult {
    object Success : OtpResult()
    object Expired : OtpResult()
    object TooManyAttempts : OtpResult()
    data class Invalid(val attemptsLeft: Int) : OtpResult()
}
