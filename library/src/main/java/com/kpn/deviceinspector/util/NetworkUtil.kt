package com.kpn.deviceinspector.util

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log

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
            Log.e("NetworkUtil", "Missing location permission to read Wi-Fi SSID")
            ""
        } catch (e: Exception) {
            Log.e("NetworkUtil", "Error getting Wi-Fi SSID: ${e.localizedMessage}")
            ""
        }
    }
}