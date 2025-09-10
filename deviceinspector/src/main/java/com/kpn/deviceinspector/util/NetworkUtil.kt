package com.kpn.deviceinspector.util

import android.content.Context
import android.net.wifi.WifiManager

object NetworkUtil {

    fun isWifiEnabled(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    @Suppress("DEPRECATION")
    fun getWifiSSID(context: Context): String {
        return try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val ssid = wifiManager.connectionInfo.ssid
            if (ssid == "<unknown ssid>") "" else ssid.removePrefix("\"").removeSuffix("\"")
        } catch (e: SecurityException) {
            DeviceInfoLogger.recordError(e,"Missing location permission to read Wi-Fi SSID")
            ""
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error getting Wi-Fi SSID")
            ""
        }
    }
}