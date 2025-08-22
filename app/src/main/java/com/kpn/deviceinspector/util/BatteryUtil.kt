package com.kpn.deviceinspector.util

import android.content.Context
import android.os.BatteryManager

object BatteryUtil {

    fun getBatteryLevel(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}