# AuthFlow – Passwordless Authentication (Android)

## Overview
AuthFlow is an Android application that implements a passwordless login flow using
Email and OTP. After successful login, the app shows a session screen that tracks
how long the user stays logged in.

The goal of this project is to demonstrate correct usage of Jetpack Compose,
state management using ViewModel, and clean separation of UI and business logic.
All logic is implemented locally without any backend.

---

## OTP Flow – How It Works

1. The user enters an email address and taps **Send OTP**
2. A 6-digit OTP is generated locally
3. The OTP:
   - Expires after 60 seconds
   - Allows a maximum of 3 validation attempts
4. OTPs are stored per email to support multiple users safely
5. If the user requests **Resend OTP**:
   - The old OTP is invalidated
   - Attempts are reset
   - The countdown timer restarts

For development purposes, the OTP is displayed via a local notification and Logcat.
In a real application, OTP delivery would be handled via email or SMS.

---

## OTP Validation & Error Handling

The app explicitly handles the following cases:
- Incorrect OTP input
- OTP expiry
- Exceeded maximum attempts
- Resend OTP after failure or expiry

Each failure case updates the UI with a clear error message and logs an analytics event.

---

## Session Handling

After successful OTP verification:
- The session start time is recorded
- A live session duration (mm:ss) is displayed
- The timer updates every second using a coroutine
- Logging out stops the timer and resets session state

The timer logic is lifecycle-safe and does not create multiple running coroutines.

---

## Architecture & State Management

The app follows an MVVM architecture with one-way data flow:
- ViewModel owns all business logic
- UI observes state exposed via StateFlow
- Composables are stateless and react to state changes

OTP logic is isolated in a dedicated manager class to keep responsibilities clear
and avoid mixing UI and business logic.

---

## Analytics Integration

Firebase Analytics is integrated to track important user actions:
- OTP generated
- OTP resent
- OTP validation success
- OTP validation failure (with reason)
- Logout

Analytics calls are triggered from the ViewModel layer to maintain separation
of concerns. Events were verified using Firebase DebugView.

---

## Splash Screen

A simple splash screen is implemented using Jetpack Compose.
It displays the app branding briefly before navigating to the main flow.
This approach keeps the implementation simple and easy to understand.

---

## Project Structure

