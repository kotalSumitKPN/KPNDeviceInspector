package com.kpn.android.deviceinspector.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

object DeviceInfoLogger {

    private const val TAG = "Device API"

    fun log(key: String, value: Any) {
        FirebaseCrashlytics.getInstance()
            .setCustomKey("$TAG: $key", value.toString())
    }

    fun recordError(error: Throwable, customMessage: String? = null) {
        if (customMessage != null) {
            FirebaseCrashlytics.getInstance()
                .log("$TAG: $customMessage")
        }
        FirebaseCrashlytics.getInstance()
            .recordException(error)
    }
}