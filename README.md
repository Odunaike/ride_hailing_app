# Ride Hailing App

A modern ride-hailing application built with Kotlin and Jetpack Compose, leveraging the Google Maps Platform for a seamless user experience.

## Table of Contents

-   [Features](#features)
-   [Technologies Used](#technologies-used)
-   [Getting Started](#getting-started)
    -   [Prerequisites](#prerequisites)
    -   [Installation](#installation)
    -   [Configuration](#configuration)
-   [Usage](#usage)
-   

## Features
-   **User Location:** Real-time tracking of the user's current location.
-   **Location Selection:** Ability to select a destination using a search bar.
-   **Map Integration:** Display of user location and selected destination on a Google Map.
-   **Route Display:** Drawing of a polyline route between the user's location and the selected destination.
-   **Distance Calculation:** Calculation and display of the distance between the user and the selected destination.
-   **Ride Fare Estimation:** Estimation of the ride fare based on the distance.
-   **Bottom Sheet:** Display of ride fare details in a modal bottom sheet.
-   **Peak Hour Consideration**: The app will take into account the peak hours when calculating the fare.
-   **Permission Handling:** Proper handling of location permission requests.
-   **Edge-to-Edge**: The app is edge-to-edge, meaning that the content will be displayed behind the system bars.

## Technologies Used

**Kotlin:** The primary programming language for Android development.
-   **Jetpack Compose:** Modern UI toolkit for building native Android UIs.
-   **Google Maps Platform:**
    -   **Maps SDK for Android:** Displaying maps and markers.
    -   **Directions API:** Calculating routes and travel times.
-   **Retrofit:** A type-safe HTTP client for Android and Java.
-   **Timber:** A logger with a small, extensible API.
-   **AndroidX Libraries:**
    -   **Activity Compose:** Integration of Compose with Activities.
    -   **Compose Material 3:** Material Design 3 components.
    -   **Compose Foundation:** Core building blocks for Compose UI.
    -   **Compose Runtime:** Core runtime for Compose.
    -   **Lifecycle Runtime:** Managing component lifecycles.
-   **Google Maps Compose:** Jetpack Compose integration for the Google Maps SDK.
-   **Google Location Services:** For fetching the user's location.
- **PolyUtils**: For decoding the polyline.
- **SphericalUtils**: For calculating the distance.

## Getting Started
- Clone the app from GitHub:
- Get google map api key from https://developers.google.com/maps/documentation/android-sdk/get-api-key
- Replace the 'TODO' string in the AndroidManifest file
- Enable Directions API in the Google Cloud Console
- Get the Directions API key from https://developers.google.com/maps/documentation/directions/get-api-key
- Replace directionApiKey variable declaration in the MapScreen.kt file

**Run the App:**
-   Connect an Android device or start an emulator.
-   In Android Studio, click the "Run" button (the green play icon).
2. **Grant location permission**:
    - The app will ask you to grant location permission.
3.  **Select a Destination:**
    -   Use the search bar to select a destination.
4.  **View the Route:**
    -   A polyline route will be displayed on the map between your current location and the selected destination.
5.  **View the Fare:**
    - Click on the "Request Ride" button to view the fare.