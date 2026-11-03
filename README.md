# MobiBank

MobiBank is a Kotlin Android application scaffold organized into `app`, `domain`, and `data` Gradle modules. The current app launches a Jetpack Compose screen with a Material 3 theme and a sample greeting; banking workflows, domain logic, and data sources are not implemented yet.

## Screenshots

| Main screen | Additional screen |
|:---:|:---:|
| <img src="/docs/images/screen1.png" alt="MobiBank main screen" width="280"> | <img src="/docs/images/screen2.png" alt="MobiBank screen placeholder" width="280"> |

> Replace these placeholders with captured app screenshots when available.

## Architecture

The project has a **layered, multi-module foundation** rather than a fully realized Clean Architecture or MVVM implementation. The modules establish dependency direction between the Android application, a platform-independent domain library, and an Android data library, but the latter two currently contain no production Kotlin implementation. The app entry point is `MainActivity`; it sets Compose content using `MobiBankTheme`, a `Scaffold`, and the sample `Greeting` composable. There are no ViewModels or use cases in the current source tree.

### Modules and dependency rules

| Module | Responsibility | Depends on / provides |
|---|---|---|
| `:app` | Android application entry point, Compose UI, theme, resources, and APK packaging. | Depends on `:domain` and `:data`; provides the launcher application (`com.example.mobibank`). |
| `:domain` | Intended location for platform-independent business rules and domain contracts. | Currently a Kotlin/JVM library with no production source files; has no project-module dependencies. It should remain independent of Android and implementation details. |
| `:data` | Intended location for data-source and repository implementations. | Android library that depends on `:domain`; currently has no production source files. Implementations should satisfy contracts defined in `:domain`. |

Dependency direction in the current Gradle setup:

```text
:app ─────► :data ─────► :domain
  └────────────────────► :domain
```

Keep business rules and contracts in `:domain`, implement data access in `:data`, and let `:app` compose the UI and application graph. The present code does not yet include repository contracts, persistence, network clients, dependency injection, or an application-level state-management layer.

![Architecture Diagram](docs/images/architecture_diagram.png)

## Tech Stack

Versions are centralized in [`gradle/libs.versions.toml`](gradle/libs.versions.toml). The lists below reflect declared Gradle dependencies and plugins, not planned integrations.

| Category | Technologies |
|---|---|
| Language and build | Kotlin 2.2.10, Gradle Kotlin DSL, Gradle Wrapper 9.4.1, Android Gradle Plugin 9.2.1; Java/Kotlin bytecode target 11 for modules, Gradle daemon JDK toolchain 21. |
| UI | Jetpack Compose (Compose BOM `2026.02.01`), Material 3, Activity Compose, AndroidX Core KTX; custom Compose theme with system dark mode and Android 12+ dynamic color support. |
| Architecture | `:app`, `:domain` (Kotlin/JVM), and `:data` (Android library) module separation. No MVVM/ViewModel, navigation, or use-case framework is currently declared. |
| Dependency injection | None declared. |
| Local / remote data | None declared. There is no database, persistence library, HTTP client, or API integration in the current Gradle dependencies. |
| Security | No security or cryptography library is declared; no authentication or banking security flows are implemented. |
| Asynchrony | No coroutine or reactive-stream dependency is declared. |
| Testing | JUnit 4, AndroidX JUnit, Espresso, and Compose UI testing dependencies. Existing test classes are starter smoke/example tests. |

The Android application is configured with `compileSdk` 36 (minor API level 1), `targetSdk` 36, and `minSdk` 28. Its application version is currently `1.0` (`versionCode` 1).

## Setup & Build Instructions

### Prerequisites

- **JDK 21**: the repository's Gradle daemon JVM configuration requests toolchain version 21. Gradle can resolve the configured toolchain through the Foojay resolver when needed.
- **Android Studio** with support for Android Gradle Plugin 9.2.1, and Android SDK Platform 36.1 plus Build Tools installed through SDK Manager.
- An Android emulator or device running **Android 9 (API 28) or newer** to install and run the app.

### Clone and build

```bash
git clone https://github.com/WantonOoze23/MobiBank.git
cd MobiBank

# Build all modules and variants
./gradlew build

# Build the debug APK
./gradlew :app:assembleDebug
```

On Windows, use `gradlew.bat` instead of `./gradlew`. Open the project root in Android Studio and allow Gradle sync to complete to work with the IDE.

### Run

Start an API 28+ emulator or connect an Android device with USB debugging enabled, then run:

```bash
./gradlew :app:installDebug
adb shell am start -n com.example.mobibank/.MainActivity
```

The current launcher screen is a Compose starter screen that displays “Hello Android!”.

## Testing

Run local JVM unit tests for the app and data module with:

```bash
./gradlew test
```

Run connected instrumented tests on an available emulator/device with:

```bash
./gradlew connectedCheck
```

The repository currently includes basic example tests in `app/src/test`, `app/src/androidTest`, `data/src/test`, and `data/src/androidTest`. They verify starter arithmetic or package-context behavior; they do not yet exercise MobiBank domain behavior. The `:domain` module currently has no test sources.
