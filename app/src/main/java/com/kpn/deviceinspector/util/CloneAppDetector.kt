package com.kpn.deviceinspector.util

import android.content.Context
import android.os.Process
import android.util.Log

object CloneAppDetector {

    fun isAppCloned(context: Context): Boolean {
        return try {
            val dataDir = context.applicationInfo.dataDir
            val packageName = context.packageName

            val isDataDirCloned = !dataDir.contains("/data/user/0/$packageName")
            val isUserIdHigh = Process.myUid() / 100000 >= 999
            val isPackageInWeirdPath = dataDir.contains("dual") || dataDir.contains("clone")

            if (isDataDirCloned || isUserIdHigh || isPackageInWeirdPath) {
                Log.e("CloneAppDetector", "Clone app environment detected.")
                true
            } else false

        } catch (e: Exception) {
            Log.e("CloneAppDetector", "Error detecting clone app: ${e.message}")
            false
        }
    }
}