package com.kpn.deviceinspector.util

import android.os.SystemClock
import android.util.Log

object FactoryResetDetector {

    fun isRecentlyFactoryReset(thresholdInHours: Long = 24): Boolean {
        return try {
            val uptime = SystemClock.elapsedRealtime()
            val bootTimeMillis = System.currentTimeMillis() - uptime

            val timeSinceBootHours = (System.currentTimeMillis() - bootTimeMillis) / (1000 * 60 * 60)

            if (timeSinceBootHours < thresholdInHours) {
                Log.e("FactoryResetDetector", "Possible recent factory reset (uptime: $timeSinceBootHours hours)")
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }
}