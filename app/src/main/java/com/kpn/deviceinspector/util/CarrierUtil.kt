package com.kpn.deviceinspector.util

import android.content.Context
import android.telephony.TelephonyManager

object CarrierUtil {
    fun getCarrierName(context: Context): String {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.networkOperatorName ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}