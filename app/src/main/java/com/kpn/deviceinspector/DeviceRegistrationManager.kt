package com.kpn.deviceinspector

import android.content.Context
import com.kpn.deviceinspector.model.DeviceRegisterResponse
import com.kpn.deviceinspector.network.DeviceApi

object DeviceRegistrationManager {

    suspend fun registerDevice(
        context: Context,
        token: String,
        anonymousUserId: String,
        userId: String,
        customerId: String,
        deviceId: String?,
        fcmId: String,
        createdAt: String,
        createdByApp: String,
        createdByUser: String,
        sessionAt: String,
        sessionByApp: String,
        sessionByUser: String,
        expectedSignatureSha256: String
    ): DeviceRegisterResponse? {
        val deviceInfo = DeviceInspector.collect(
            context = context,
            anonymousUserId = anonymousUserId,
            userId = userId,
            deviceId = deviceId ?: "",
            fcmId = fcmId,
            createdAt = createdAt,
            createdByApp = createdByApp,
            createdByUser = createdByUser,
            sessionAt = sessionAt,
            sessionByApp = sessionByApp,
            sessionByUser = sessionByUser,
            expectedSignatureSha256 = expectedSignatureSha256,
            customerId = customerId
        )

        val modifiedDeviceInfo = if (deviceId.isNullOrBlank()) {
            deviceInfo.copy(device_id = null)
        } else {
            deviceInfo
        }

        return DeviceApi.registerDevice(token, modifiedDeviceInfo)
    }

    suspend fun registerAndSaveDeviceId(
        context: Context,
        token: String,
        anonymousUserId: String,
        userId: String,
        deviceId: String?,
        fcmId: String,
        createdAt: String,
        createdByApp: String,
        createdByUser: String,
        sessionAt: String,
        sessionByApp: String,
        sessionByUser: String,
        expectedSignatureSha256: String,
        customerId: String,
        onNewDeviceId: (String) -> Unit
    ): DeviceRegisterResponse? {
        val response = registerDevice(
            context = context,
            token = token,
            anonymousUserId = anonymousUserId,
            userId = userId,
            deviceId = deviceId,
            fcmId = fcmId,
            createdAt = createdAt,
            createdByApp = createdByApp,
            createdByUser = createdByUser,
            sessionAt = sessionAt,
            sessionByApp = sessionByApp,
            sessionByUser = sessionByUser,
            expectedSignatureSha256 = expectedSignatureSha256,
            customerId = customerId
        )

        response?.deviceId?.let { newId ->
            if (deviceId.isNullOrBlank()) {
                onNewDeviceId(newId)
            }
        }

        return response
    }
}