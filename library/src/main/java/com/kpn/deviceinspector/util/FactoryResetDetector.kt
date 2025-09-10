package com.kpn.android.deviceinspector.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import java.io.File
import java.security.MessageDigest

object FactoryResetDetector {

    private const val TAG = "FactoryResetDetector"
    private const val PREF_NAME = "com.kpn.deviceinspector.prefs"
    private const val KEY_ANDROID_ID_HASH = "kpn_android_id_hash"
    private const val MARKER_FILE_NAME = ".kpn_device_marker"

    @Volatile
    private var hasBeenChecked = false

    fun isSuspiciousFactoryReset(context: Context): Boolean {
        if (hasBeenChecked) return false
        hasBeenChecked = true

        val isFreshBoot = isDeviceRecentlyBooted()
        val isFreshInstall = isAppRecentlyInstalled(context)
        val isMarkerMissing = isMarkerFileMissing(context)
        val isAndroidIdChanged = isAndroidIdHashChanged(context)

        Log.d(TAG, "freshBoot=$isFreshBoot, freshInstall=$isFreshInstall, markerMissing=$isMarkerMissing, androidIdChanged=$isAndroidIdChanged")

        val score = listOf(isFreshBoot, isFreshInstall, isMarkerMissing, isAndroidIdChanged).count { it }

        return score >= 2
    }

    private fun isDeviceRecentlyBooted(thresholdInHours: Long = 24): Boolean {
        return try {
            val uptime = SystemClock.elapsedRealtime()
            val bootTimeMillis = System.currentTimeMillis() - uptime
            val timeSinceBootHours = (System.currentTimeMillis() - bootTimeMillis) / (1000 * 60 * 60)

            timeSinceBootHours < thresholdInHours
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error getting device uptime")
            false
        }
    }

    private fun isAppRecentlyInstalled(context: Context, thresholdInHours: Long = 24): Boolean {
        return try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            val installTime = info.firstInstallTime
            val diff = System.currentTimeMillis() - installTime
            val diffHours = diff / (1000 * 60 * 60)

            diffHours < thresholdInHours
        } catch (e: PackageManager.NameNotFoundException) {
            DeviceInfoLogger.recordError(e,"Error getting app install time")
            false
        }
    }

    private fun isMarkerFileMissing(context: Context): Boolean {
        return try {
            val markerFile = File(context.filesDir, MARKER_FILE_NAME)
            if (!markerFile.exists()) {
                markerFile.createNewFile()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error checking marker file")
            true
        }
    }

    private fun isAndroidIdHashChanged(context: Context): Boolean {
        return try {
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: return false
            val androidIdHash = sha256(androidId)

            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val storedHash = prefs.getString(KEY_ANDROID_ID_HASH, null)

            return if (storedHash == null) {
                prefs.edit().putString(KEY_ANDROID_ID_HASH, androidIdHash).apply()
                false
            } else {
                androidIdHash != storedHash
            }
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error checking Android ID hash")
            false
        }
    }

    private fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}