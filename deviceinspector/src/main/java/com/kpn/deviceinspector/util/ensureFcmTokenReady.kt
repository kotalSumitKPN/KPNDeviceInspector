package com.kpn.deviceinspector.util

import com.kpn.deviceinspector.bridge.FcmProvider

suspend fun ensureFcmTokenReady(
    cachedFcmToken: String?,
    fcmProvider: FcmProvider
): String {
    return cachedFcmToken ?: try {
        fcmProvider.getFcmToken()
    } catch (e: Exception) {
        DeviceInfoLogger.recordError(e, "FCM Token not ready")
        ""
    }
}