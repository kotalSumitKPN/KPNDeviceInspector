# KPNDeviceInspector

`KPNDeviceInspector` is an Android library for advanced device metadata collection and integrity checks. It helps detect tampering, spoofing, emulator use, root access, and more to aid in fraud prevention and secure device registration.

---


[![](https://jitpack.io/v/kotalSumitKPN/KPNDeviceInspector.svg)](https://jitpack.io/#kotalSumitKPN/KPNDeviceInspector)

### Installation

### Step 1: Add JitPack to your root `settings.gradle` or `settings.gradle.kts`
```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

### Step 2: Add library dependency to your `build.gradle.kts`
```kotlin
// Replace <latest-version> with the version shown on the badge or JitPack
dependencies {
    implementation("com.github.kotalSumitKPN:KPNDeviceInspector:<latest-version>")
}
```

---

## App-Side Usage

### Direct call (returns response):

```kotlin
val response = DeviceRegistrationManager.registerDevice(
    context = applicationContext,
    token = YOUR_JWT_TOKEN,
    anonymousUserId = UUID.randomUUID().toString(),
    userId = ACTUAL_USER_ID,
    customerId = CUSTOMER_ID,
    deviceId = DEVICE_ID, // nullable
    fcmId = FCM_TOKEN,
    createdAt = ISO_DATE_STRING,
    createdByApp = APP_NAME,
    createdByUser = ACTUAL_USER_ID,
    sessionAt = ISO_DATE_STRING,
    sessionByApp = APP_NAME,
    sessionByUser = ACTUAL_USER_ID,
    expectedSignatureSha256 = YOUR_SHA256_SIGNATURE
)
```

### Auto-saving `device_id` using a callback:

```kotlin
fun registerDevice() {
    lifecycleScope.launch {
        DeviceRegistrationManager.registerAndSaveDeviceId(
            context = applicationContext,
            token = "YOUR_JWT_TOKEN",
            anonymousUserId = "UUID_STRING",
            userId = "USER_ID",
            deviceId = null,
            fcmId = "FCM_TOKEN",
            createdAt = "ISO_DATE",
            createdByApp = "KPN_FRESH",
            createdByUser = "USER_ID",
            sessionAt = "ISO_DATE",
            sessionByApp = "KPN_FRESH",
            sessionByUser = "USER_ID",
            expectedSignatureSha256 = "YOUR_SHA256",
            customerId = "CUSTOMER_ID",
            onNewDeviceId = { newId ->
                println("Saving new Id - $newId")
            }
        )
    }
}
```

---

## API Response

The library returns:

```json
{
  "device_id": "d385b303-9abf-5870-87c7-f9e8f083adff"
}
```

This `device_id` must be saved and reused for future sessions.

---

## DeviceRegisterRequest – Field-by-Field

| Field | Description |
|-------|-------------|
| `anonymous_user_id` | UUID for guest session |
| `user_id` | Logged-in user ID |
| `device_id` | Firebase/App Installation ID |
| `customer_id` | Customer or backend ref |
| `platform` | "AND" for Android |
| `fcm_id` | Firebase token |
| `created_at` / `session_at` | ISO timestamps |
| `created_by_app` / `session_by_app` | App origin (e.g., KPN_FRESH) |
| `created_by_user` / `session_by_user` | User metadata |
| `brand`, `model`, `os_version` etc. | Device info |
| `user_language`, `user_region` | Locale data |
| ... and many more |

---

## Security Checks & How They Work

| Check | How it's detected |
|-------|--------------------|
| `is_app_tampered` | Verifies app signature vs expected |
| `is_auto_clicker_enabled` | Detects known auto-clicker APKs |
| `is_debugging_enabled` | `Settings.Global.ADB_ENABLED` |
| `is_hooking` | Detects Frida/Substrate hooks |
| `is_emulator` | Checks build props, telephony |
| `is_jail_broken` | Looks for root binaries |
| `is_running_clone_app` | Checks dual app environment |
| `is_running_gps_spoofer` | Uses `isFromMockProvider` |
| `is_virtual_os` | VMWare/VirtualBox props |
| `is_secondary_user` | Multi-user mode check |
| `is_suspicious_factory_reset` | Factory reset history |
| ... and more |

---

## 📁 Folder Structure

```
com.kpn.deviceinspector
│
├── data/                    # Ktor DTO models
│   ├── DeviceRegisterRequest.kt
│   └── DeviceRegisterResponse.kt
│
├── network/                # Ktor client
│   └── KtorClientProvider.kt
│
├── util/                   # Detection utilities
│   ├── AppSignature.kt
│   ├── AutoClickerUtil.kt
│   ├── EmulatorCheckUtil.kt
│   ├── GpsSpooferDetector.kt
│   ├── RootDetection.kt
│   ├── TamperCheckUtil.kt
│   └── UserAgentUtil.kt
│
└── DeviceInspector.kt      # Collects all device metadata
└── DeviceRegistrationManager.kt  # Final API manager
```

---

## Best Practices

- Always call `registerDevice()` after login or on launch
- Save the `device_id` locally
- Pass SHA-256 of your app for tamper verification
- Ensure your backend expects and logs this metadata

---

## License

MIT License — © [@kotalSumitKPN](https://github.com/kotalSumitKPN)
