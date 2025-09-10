package com.kpn.android.deviceinspector.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

object VpnSpooferDetector {

    fun isVpnActive(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            val isVpn = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

            if (isVpn) {
                DeviceInfoLogger.log("VpnSpooferDetector","VPN connection detected")
            }
            isVpn
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error detecting VPN")
            false
        }
    }
}