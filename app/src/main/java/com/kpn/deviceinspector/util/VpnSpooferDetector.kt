package com.kpn.deviceinspector.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log

object VpnSpooferDetector {

    fun isVpnActive(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            val isVpn = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

            if (isVpn) {
                Log.e("VpnSpooferDetector", "VPN connection detected")
            }
            isVpn
        } catch (e: Exception) {
            Log.e("VpnSpooferDetector", "Error detecting VPN: ${e.message}")
            false
        }
    }
}