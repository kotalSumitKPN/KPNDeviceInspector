package com.kpn.android.deviceinspector.util

import android.content.Context
import android.telephony.TelephonyManager

object CarrierUtil {

    private const val PLACEHOLDER = "unrecognized"
    fun getCarrierName(context: Context): String {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.networkOperatorName ?: ""
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e, "Error getting carrier name")
            PLACEHOLDER
        }
    }
}