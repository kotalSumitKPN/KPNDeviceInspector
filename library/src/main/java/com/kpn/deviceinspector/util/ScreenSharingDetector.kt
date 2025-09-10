package com.kpn.android.deviceinspector.util

import android.content.Context
import android.hardware.display.DisplayManager
import android.util.Log

object ScreenSharingDetector {

    fun isScreenSharingActive(context: Context): Boolean {
        return try {
            val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val displays = displayManager.displays

            val virtualDisplays = displays.filter {
                it.name.lowercase().contains("virtual") || it.name.lowercase().contains("cast") || it.name.lowercase().contains("screen")
            }

            if (virtualDisplays.isNotEmpty()) {
                DeviceInfoLogger.log("ScreenSharingDetector","Virtual display(s) active: ${virtualDisplays.map { it.name }}")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error detecting screen sharing")
            false
        }
    }
}