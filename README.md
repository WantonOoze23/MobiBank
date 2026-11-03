# MobiBank

MobiBank is an Android application organized around banking-related features, including accounts, authentication, deposits, loans, profiles, and transactions. It uses Jetpack Compose for its UI and separates presentation, domain contracts, and data implementations across Gradle modules.

## Screenshots

| Home | Accounts |
|---|---|
| ![Home screen](/docs/images/screen1.png) | ![Accounts screen](/docs/images/screen2.png) |

| Transactions | Profile |
|---|---|
| ![Transactions screen](/docs/images/screen3.png) | ![Profile screen](/docs/images/screen4.png) |

> The image paths above are placeholders; add the corresponding screenshots under `docs/images/`.

## Architecture

MobiBank uses a **layered, multi-module architecture** with a Compose-based presentation layer. The `domain` module defines models, repository contracts, and use cases; `data` implements those contracts and contains local and network data code; `app` hosts the Android application, UI, navigation, and dependency-injection setup. This structure keeps domain contracts independent of the Android app and data implementations.

### Modules

| Module | Responsibility | Dependencies and outputs |
|---|---|---|
| `:app` | Android application entry point, Compose UI, feature screens, navigation, and app-level dependency injection. Feature packages include `accounts`, `auth`, `deposits`, `loans`, `profile`, and `transactions`. | Depends on `:domain` and `:data`; packages the runnable Android app. |
| `:domain` | Kotlin/JVM module containing domain models, repository interfaces, and use cases. | Has no project-module dependencies; provides contracts consumed by `:app` and implemented by `:data`. |
| `:data` | Android library for repository implementations, API/network code, DTOs, mappers, local DataStore code, and data-layer dependency injection. | Depends on `:domain`; provides implementations and data sources to `:app`. |

Dependency direction: `:app` → `:domain` and `:data`; `:data` → `:domain`. The domain module does not depend on the Android app or data layer.

![Architecture Diagram](docs/images/architecture_diagram.png)

### Source layout

```text
app/src/main/java/com/example/mobibank/
├── di/          App and domain Koin modules
├── feature/     Accounts, auth, deposits, loans, profile, transactions
├── ui/          Shared components, navigation, and theme
├── MainActivity.kt
└── MobiBankApplication.kt

data/src/main/java/com/example/data/
├── di/          Data Koin module
├── local/       DataStore-backed local data
├── mapper/      Domain/data model mapping
├── network/     API interfaces, DTOs, and interceptors
└── repository/  Repository implementations

domain/src/main/java/com/example/domain/
├── model/       Domain models
├── repository/  Repository contracts
└── usecase/     Domain use cases
```

## Tech Stack

Versions are managed in `gradle/libs.versions.toml`.

- **Language and build:** Kotlin 2.2.10, Gradle Kotlin DSL, Gradle wrapper 9.4.1, Android Gradle Plugin 9.2.1.
- **UI:** Jetpack Compose, Material 3, Material icons, Activity Compose.
- **Architecture and navigation:** AndroidX Lifecycle ViewModel/Compose integrations, Navigation Compose.
- **Dependency injection:** Koin Android and Koin Compose.
- **Local data:** Preferences DataStore.
- **Remote data and serialization:** Retrofit, OkHttp, OkHttp logging interceptor, Kotlin serialization JSON, Retrofit Kotlin serialization converter.
- **Android platform:** AndroidX Core KTX, AppCompat, Material Components.
- **Testing libraries:** JUnit 4, AndroidX Test JUnit, Espresso, and Compose UI testing.
- **Security:** No dedicated security library is declared in the version catalog. The app manifest currently sets `android:usesCleartextTraffic="true"`; review this setting before using the app with sensitive data or production endpoints.
- **Asynchrony:** No separate coroutine library is explicitly declared in the version catalog. Check the APIs used by the implementations and their dependency graph when making concurrency assumptions.

## Setup & Build

### Requirements

- **JDK 21** is selected by the Gradle daemon JVM configuration.
- Android SDK with **API 36** and the configured API 36 minor level available.
- Android Studio version compatible with the project's Android Gradle Plugin and SDK configuration.
- An Android device or emulator running **Android 9 (API 28) or later** (`minSdk = 28`).

### Clone and open

```bash
git clone https://github.com/WantonOoze23/MobiBank.git
cd MobiBank
git checkout dev
```

Open the project directory in Android Studio and allow Gradle sync to complete. The Gradle wrapper downloads the configured Gradle distribution on its first run.

### Build and run

Build the debug APK:

```bash
./gradlew assembleDebug
```

Install the debug build on a connected device or running emulator:

```bash
./gradlew installDebug
```

On Windows, use `gradlew.bat` in place of `./gradlew`. You can also select the `app` run configuration in Android Studio.

## Testing

Run the configured unit-test tasks:

```bash
./gradlew test
```

Run instrumented tests on a connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

The app and data module Gradle files declare JUnit and Android instrumentation test dependencies. Use the commands above to execute the tests available in the checked-out project.
