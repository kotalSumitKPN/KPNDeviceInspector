package com.kpn.deviceinspector.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import java.security.MessageDigest

object TamperCheckUtil {

    fun isAppTampered(context: Context, expectedSignatureSha256: String): Boolean {
        return try {
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val info = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                info.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                val info = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                )
                info.signatures
            }

            val signatureBytes = signatures?.firstOrNull()?.toByteArray()
            if (signatureBytes == null) {
                DeviceInfoLogger.log("TamperCheck","Signature byte array is null")
                false
            } else {
                val digest = MessageDigest.getInstance("SHA-256")
                val actualHash = Base64.encodeToString(digest.digest(signatureBytes), Base64.NO_WRAP)
                actualHash != expectedSignatureSha256
            }
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error checking app signature")
            false
        }
    }
}