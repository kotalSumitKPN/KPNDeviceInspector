package com.kpn.deviceinspector.util

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings

object GpsSpooferDetector {

    fun isMockLocationEnabled(context: Context): Boolean {
        return try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.allProviders

            for (provider in providers) {
                val lastKnownLocation: Location? = try {
                    locationManager.getLastKnownLocation(provider)
                } catch (e: SecurityException) {
                    DeviceInfoLogger.recordError(e, "Error getting last known location")
                    null
                }

                if (lastKnownLocation != null) {
                    val isMock = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        lastKnownLocation.isFromMockProvider
                    } else {
                        isAllowMockLocationEnabled(context)
                    }

                    if (isMock) {
                        return true
                    }
                }
            }
            isAllowMockLocationEnabled(context)
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e, "Error detecting mock location")
            false
        }
    }

    @Suppress("DEPRECATION")
    private fun isAllowMockLocationEnabled(context: Context): Boolean {
        return try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION) != 0
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e, "Error checking allow mock location")
            false
        }
    }
}