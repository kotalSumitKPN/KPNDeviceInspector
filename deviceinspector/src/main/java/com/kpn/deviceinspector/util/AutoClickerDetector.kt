package com.kpn.deviceinspector.util

import android.content.Context
import android.content.pm.PackageManager

object AutoClickerDetector {

    private val knownAutoClickers = listOf(
        "com.truedevelopersstudio.automate",
        "com.android.clickmate",
        "com.kok_emm.mobileautoclicker",
        "com.arlosoft.macrodroid",
        "com.atejapps.autoclicker",
        "com.okautoclicker.autoclicker"
    )

    fun isAutoClickerInstalled(context: Context): Boolean {
        val pm = context.packageManager
        return knownAutoClickers.any { packageName ->
            try {
                pm.getPackageInfo(packageName, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                DeviceInfoLogger.recordError(
                    e,
                    "AutoClickerDetector: Package $packageName not found"
                )
                false
            }
        }
    }
}