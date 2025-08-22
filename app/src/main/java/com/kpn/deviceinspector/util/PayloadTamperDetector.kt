package com.kpn.deviceinspector.util

import android.os.Build
import android.util.Log

object PayloadTamperDetector {

    fun isPayloadTampered(): Boolean {
        return try {
            if (HookingDetector.isHookingDetected()) {
                Log.e("PayloadTamperDetector", "Detected active hooking tools (Frida/Xposed)")
                return true
            }

            val suspiciousFields = listOf(
                Build.MODEL,
                Build.MANUFACTURER,
                Build.BRAND,
                Build.DEVICE,
                Build.HARDWARE
            ).any { it.isNullOrBlank() || it.lowercase() in listOf("unknown", "android", "generic") }

            if (suspiciousFields) {
                Log.e("PayloadTamperDetector", "Detected modified or masked device identifiers")
                return true
            }

            false
        } catch (e: Exception) {
            Log.e("PayloadTamperDetector", "Error in payload tamper detection: ${e.message}")
            false
        }
    }
}