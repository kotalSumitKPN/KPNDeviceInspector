package com.kpn.deviceinspector.util

import android.os.Build

object DeviceMaskingDetector {

    fun isDeviceMasked(): Boolean {
        return listOf(
            Build.MODEL,
            Build.MANUFACTURER,
            Build.BRAND,
            Build.DEVICE,
            Build.PRODUCT,
            Build.HARDWARE
        ).any { it.isNullOrBlank() || it.equals("unknown", ignoreCase = true) || it.equals("android", ignoreCase = true) }
    }
}