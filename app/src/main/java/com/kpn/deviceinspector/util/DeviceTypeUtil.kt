package com.kpn.deviceinspector.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.sqrt

object DeviceTypeUtil {

    fun getDeviceType(context: Context): String {
        val pm = context.packageManager

        return when {
            pm.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE) -> "automotive"
            pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK) -> "tv"
            pm.hasSystemFeature(PackageManager.FEATURE_WATCH) -> "watch"
            isTablet(context) -> "tablet"
            else -> "smartphone"
        }
    }

    private fun isTablet(context: Context): Boolean {
        val metrics = context.resources.displayMetrics
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = sqrt(widthInches * widthInches + heightInches * heightInches)
        return diagonalInches >= 7.0
    }
}