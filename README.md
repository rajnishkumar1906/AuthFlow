---

# AuthFlow – Email + OTP Authentication (Android)

## Overview

AuthFlow is an Android application that demonstrates a **passwordless authentication flow using Email and OTP**, built with **Kotlin** and **Jetpack Compose**.

The app is designed to showcase **state-driven UI**, **time-based logic**, and **clean architecture** without using any backend services. All authentication logic is handled locally for demonstration purposes.

---

## App Flow

1. User enters an email address.
2. A 6-digit OTP is generated locally.
3. User enters the OTP to verify.
4. On successful verification, the user is logged in.
5. A session screen displays the active session duration.
6. User can log out and return to the login screen.

---

## OTP Implementation

The OTP system follows realistic authentication rules:

* OTP length: **6 digits**
* OTP expiry time: **60 seconds**
* Maximum verification attempts: **3**
* OTPs are stored **per email**
* Generating a new OTP:

  * Invalidates the previous OTP
  * Resets attempt count
  * Restarts the expiry timer
* After OTP expiry or exceeding attempts, the user must wait before resending OTP

OTP data is stored in memory using a map structure, allowing independent handling for each email address.

---

## OTP Visibility (Development Only)

To make the flow testable without a backend:

* Generated OTPs are printed to **Logcat**
* OTPs are also displayed via an **Android notification**

This is strictly for development and demonstration. In a production app, OTPs would be delivered via email or SMS.

---

## Session Handling

After successful OTP verification:

* Session start time is recorded
* Session duration is updated every second and shown in **mm:ss** format
* The timer survives recompositions
* The timer stops correctly when the user logs out

---

## Architecture

The project follows **MVVM (Model–View–ViewModel)** architecture:

* **UI Layer (Jetpack Compose)**
  Displays UI based on state and forwards user actions

* **ViewModel Layer**
  Contains all authentication logic, timers, and state using `StateFlow`

* **State Layer**
  A single immutable state object drives the entire UI

This ensures one-way data flow and predictable UI behavior.

---

## Jetpack Compose Usage

The project uses the following Compose concepts:

* `@Composable`
* `remember` and `rememberSaveable`
* `LaunchedEffect`
* State hoisting
* Recomposition-safe UI updates

All business logic is kept out of composables.

---

## Firebase Analytics

Firebase Analytics is integrated to track important authentication events:

* OTP generated
* OTP resent
* OTP validation success
* OTP validation failure
* Logout

Events were verified during development using **Firebase Analytics DebugView**.

---

## Edge Cases Handled

* Expired OTP
* Incorrect OTP
* Exceeded attempt limit
* Resend OTP flow
* Retry cooldown handling
* Screen rotation without state loss

---

## Project Structure

```
ui/
 ├── LoginScreen.kt
 ├── OtpScreen.kt
 ├── SessionScreen.kt
 ├── SplashScreen.kt

viewmodel/
 ├── AuthViewModel.kt
 ├── AuthState.kt

data/
 ├── OtpManager.kt

analytics/
 ├── AnalyticsLogger.kt
```

---

## Notes

* No global mutable state is used
* No blocking calls on the main thread
* UI logic is separated from business logic
* All logic is implemented locally for clarity and learning purposes

---
