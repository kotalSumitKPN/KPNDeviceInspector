package com.kpn.android.deviceinspector.util

import android.os.Build
import android.util.Log

object VirtualOsDetector {

    fun isRunningInVirtualOs(): Boolean {
        return try {
            val indicators = listOf(
                Build.FINGERPRINT,
                Build.MODEL,
                Build.MANUFACTURER,
                Build.BRAND,
                Build.DEVICE,
                Build.PRODUCT
            ).joinToString(" ").lowercase()

            val suspectedTerms = listOf(
                "vbox", "virtual", "test-keys", "generic", "emulator", "sdk", "simulator"
            )

            val result = suspectedTerms.any { indicators.contains(it) }

            if (result) {
                DeviceInfoLogger.log("VirtualOsDetector","Virtual OS environment detected")
            }

            result
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error detecting virtual OS environment")
            false
        }
    }
}