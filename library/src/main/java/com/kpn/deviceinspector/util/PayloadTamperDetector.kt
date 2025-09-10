package com.kpn.android.deviceinspector.util

import android.os.Build
import android.util.Log

object PayloadTamperDetector {

    fun isPayloadTampered(): Boolean {
        return try {
            if (HookingDetector.isHookingDetected()) {
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
                return true
            }
            false
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error in payload tamper detection")
            false
        }
    }
}