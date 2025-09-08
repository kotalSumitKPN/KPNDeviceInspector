package com.kpn.deviceinspector.util

import android.os.Build

object EmulatorChecker {

    fun isEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT.lowercase()
        val model = Build.MODEL.lowercase()
        val product = Build.PRODUCT.lowercase()
        val brand = Build.BRAND.lowercase()
        val device = Build.DEVICE.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()
        val hardware = Build.HARDWARE.lowercase()

        return (fingerprint.contains("generic") ||
                fingerprint.contains("vbox") ||
                fingerprint.contains("test-keys") ||
                model.contains("google_sdk") ||
                model.contains("emulator") ||
                model.contains("android sdk built for") ||
                brand.startsWith("generic") && device.startsWith("generic") ||
                manufacturer.contains("genymotion") ||
                product.contains("sdk") ||
                product.contains("google_sdk") ||
                hardware.contains("goldfish") ||
                hardware.contains("ranchu"))
    }
}