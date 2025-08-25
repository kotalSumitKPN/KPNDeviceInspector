# KPNDeviceInspector

`KPNDeviceInspector` is an Android library designed to collect advanced device metadata and perform device integrity checks for registration and fraud prevention purposes. It performs detections like emulator use, root, tampering, spoofers, and more.

---

## 🚀 Installation

The library is hosted on **JitPack**. Follow these steps to integrate it into your Android app.

### Step 1: Add JitPack to your `settings.gradle`

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

### Step 2: Add the dependency in your `build.gradle`

```kotlin
dependencies {
    implementation("com.github.kotalSumitKPN:KPNDeviceInspector:v1.0.0")
}
```

---

## 📦 How to Use

Use the following function to register the device from anywhere in your app (ideally after login or app launch):

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
    createdByApp = "KPN_FRESH",
    createdByUser = ACTUAL_USER_ID,
    sessionAt = ISO_DATE_STRING,
    sessionByApp = "KPN_FRESH",
    sessionByUser = ACTUAL_USER_ID,
    expectedSignatureSha256 = YOUR_SHA256_SIGNATURE
)
```

The function:
- Collects full device data
- Detects tampering, emulator, root, spoofing, etc.
- Calls the remote API with structured payload

> You will get back a `DeviceRegisterResponse?`

---

## 📘 Model: `DeviceRegisterRequest`

Each key in the request is crafted for both analytics and fraud detection.

| Field | Description |
|-------|-------------|
| `anonymous_user_id` | UUID to identify guest users |
| `user_id` | Authenticated user ID |
| `device_id` | App or Firebase Installation ID |
| `customer_id` | Backend user/account/customer reference |
| `platform` | Always `"AND"` for Android |
| `fcm_id` | Firebase Cloud Messaging token |
| `created_at`, `created_by_app`, `created_by_user` | Metadata for creation |
| `session_at`, `session_by_app`, `session_by_user` | Metadata for last session |
| `brand`, `model`, `manufacturer`, `hardware`, `board`, `android_build`, `security_patch`, `os_version` | Device info |
| `display_type`, `screen_size`, `app_bundle_name`, `java_version`, `user_agent` | App & environment |
| `android_id` | Secure Android ID |
| `carrier`, `wifi_enabled`, `wifi_ssid` | Network info |
| `battery_level` | Battery % |
| `user_language`, `user_region` | Locale info |
| `is_app_tampered` | Signature mismatch |
| `is_auto_clicker_enabled` | Known auto clicker APKs installed |
| `is_developer_option_enabled`, `is_debugging_enabled` | Developer options check |
| `is_hooking` | Frida/Substrate detection |
| `is_device_masked` | Checks if device is pretending to be another |
| `is_emulator` | AVD detection |
| `is_jail_broken` | Root detection |
| `is_running_clone_app` | Cloning/Multi-app detected |
| `is_running_gps_spoofer` | Mock GPS enabled |
| `is_running_screen_sharing` | Screen sharing active |
| `is_running_vpn_spoofers` | VPN active |
| `is_secondary_user` | Secondary user profile (guest/multi-user) |
| `is_suspicious_factory_reset` | Factory reset detected in last 24 hours |
| `is_virtual_os` | VM or virtualized environment |
| `is_request_payload_tampered` | Attempts to modify request payload |

---

## 🪵 Logging

- The library uses Ktor with optional logging. To see full request/response logs, make sure to install the Ktor logging plugin in the consuming app (or extend the KtorClient used in the library).

---

## 🛠 Internals (How it works)

1. `DeviceInspector.collect(...)` gathers:
   - Device hardware/software info
   - Security flags (root, hook, emulator, etc.)
   - Network info, developer mode, etc.

2. `DeviceRegistrationManager.registerDevice(...)`:
   - Builds request body from `collect(...)`
   - Sends API request to your server endpoint via Ktor

3. On success, it returns `DeviceRegisterResponse` (you may log it, store it, or ignore)

---

## ⚠️ Tips

- Always send a valid JWT token in the `Authorization: Bearer ...` header.
- Your app's SHA-256 signature must be passed to validate tampering.
- Use `UUID.randomUUID().toString()` for guest users.
- Use `Instant.now().toString()` or an ISO date formatter for `created_at` and `session_at`.

---

## 🔒 Why This Library?

This library ensures secure, consistent, and tamper-proof device metadata collection for:
- Device binding and fraud prevention
- Multi-device login detection
- Debug vs Production monitoring
- Emulator and automation abuse detection

---

## 📄 License

MIT License. Free to use with attribution to [@kotalSumitKPN](https://github.com/kotalSumitKPN)