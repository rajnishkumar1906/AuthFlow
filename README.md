---

# AuthFlow – Email + OTP Authentication (Android)

## Overview

AuthFlow is a simple Android application that demonstrates a **passwordless authentication flow using Email and OTP**, built with **Kotlin** and **Jetpack Compose**.

All authentication logic is implemented **locally** without a backend, focusing on **state management**, **time-based logic**, and **clean separation of UI and business logic**.

---

## How the App Works

1. The user enters an email address.
2. A 6-digit OTP is generated locally.
3. The user enters the OTP to verify.
4. On successful verification, the user is logged in and taken to a session screen.
5. The session screen shows how long the user has been logged in.
6. The user can log out and return to the login flow.

---

## OTP Rules Implemented

* OTP length is **6 digits**
* OTP expires after **60 seconds**
* Maximum **3 verification attempts**
* OTP is stored **per email**
* Generating a new OTP:

  * Invalidates the previous OTP
  * Resets attempt count
  * Restarts expiry timer
* After OTP expiry or exceeding attempts, a **retry cooldown** is applied before resending

---

## Session Handling

* Session start time is recorded after successful login
* Session duration is displayed in **mm:ss** format
* Session timer updates every second
* Timer survives recompositions and stops correctly on logout

---

## Architecture

The app follows **MVVM architecture**:

* **UI (Jetpack Compose)**
  Displays state and forwards user actions

* **ViewModel**
  Contains all authentication, timer, and state logic using `StateFlow`

* **State**
  A single immutable state object drives the entire UI

This ensures predictable behavior and clean one-way data flow.

---

## Jetpack Compose Usage

The project uses:

* `@Composable`
* `remember` and `rememberSaveable`
* `LaunchedEffect`
* State hoisting
* Recomposition-safe UI updates

---

## Firebase Analytics

Firebase Analytics is integrated to track key user actions:

* OTP generated
* OTP resent
* OTP validation success
* OTP validation failure
* Logout

Events were verified during development using **Firebase DebugView**.

---

## Edge Cases Handled

* Expired OTP
* Incorrect OTP
* Exceeded attempt limit
* Resend OTP flow
* Retry cooldown handling
* Screen rotation without breaking state

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
* Analytics logic is kept separate from UI and ViewModel
* All logic is implemented locally for demonstration purposes

---
