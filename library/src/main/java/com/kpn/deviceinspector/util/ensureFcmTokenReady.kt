package com.kpn.android.deviceinspector.util

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

suspend fun ensureFcmTokenReady(cachedFcmToken: String?): String {
    return cachedFcmToken ?: try {
        FirebaseMessaging.getInstance().token.await()
    } catch (e: Exception) {
        DeviceInfoLogger.recordError(e, "FCM Token not ready")
        ""
    }
}