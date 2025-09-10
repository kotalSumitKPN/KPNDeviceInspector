package com.kpn.deviceinspector.util

import android.content.Context
import android.os.BatteryManager

object BatteryUtil {

    fun getBatteryLevel(context: Context): Int {
        return try {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } catch (e: Exception){
            DeviceInfoLogger.recordError(e, "Error getting battery level")
            0
        }

    }
}