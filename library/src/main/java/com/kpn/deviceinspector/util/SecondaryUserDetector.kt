package com.kpn.android.deviceinspector.util

import android.os.Process
import android.util.Log

object SecondaryUserDetector {

    fun isSecondaryUser(): Boolean {
        return try {
            val uid = Process.myUid()
            val userId = uid / 100000

            if (userId > 0) {
                DeviceInfoLogger.log("SecondaryUserDetector","Secondary user detected with userId: $userId")
                true
            } else false
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error detecting secondary user")
            false
        }
    }
}