package com.kpn.deviceinspector.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.provider.Settings

object DeveloperSettingsUtil {

    fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
            ) == 1
        } catch (e: Settings.SettingNotFoundException) {
            DeviceInfoLogger.recordError(e,"Settings not found")
            false
        }
    }

    fun isDebuggingEnabled(context: Context): Boolean {
        return 0 != (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE)
    }
}