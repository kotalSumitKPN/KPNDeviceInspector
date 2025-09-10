package com.kpn.deviceinspector

import android.content.Context
import com.kpn.deviceinspector.model.DeviceRegisterResponse
import com.kpn.deviceinspector.network.DeviceApi

object DeviceRegistrationManager {

    suspend fun registerDevice(
        context: Context,
        apiURL : String,
        token: String,
        anonymousUserId: String,
        userId: String,
        deviceId: String?,
        fcmId: String,
        createdByApp: String,
        expectedSignatureSha256: String
    ): DeviceRegisterResponse? {
        val deviceInfo = DeviceInspector.collect(
            context = context,
            anonymousUserId = anonymousUserId,
            userId = userId,
            deviceId = deviceId ?: "",
            fcmId = fcmId,
            createdByApp = createdByApp,
            expectedSignatureSha256 = expectedSignatureSha256,
        )

        val modifiedDeviceInfo = if (deviceId.isNullOrBlank()) {
            deviceInfo.copy(device_id = null)
        } else {
            deviceInfo
        }

        return DeviceApi.registerDevice(apiURL,token, modifiedDeviceInfo)
    }

    suspend fun registerAndSaveDeviceId(
        context: Context,
        apiURL : String,
        token: String,
        anonymousUserId: String,
        userId: String,
        deviceId: String?,
        fcmId: String,
        createdByApp: String,
        expectedSignatureSha256: String,
        onNewDeviceId: (String) -> Unit
    ): DeviceRegisterResponse? {
        val response = registerDevice(
            context = context,
            apiURL = apiURL,
            token = token,
            anonymousUserId = anonymousUserId,
            userId = userId,
            deviceId = deviceId,
            fcmId = fcmId,
            createdByApp = createdByApp,
            expectedSignatureSha256 = expectedSignatureSha256,
        )

        response?.deviceId?.let { newId ->
            if (deviceId.isNullOrBlank()) {
                onNewDeviceId(newId)
            }
        }

        return response
    }
}