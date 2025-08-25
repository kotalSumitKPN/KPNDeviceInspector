package com.kpn.deviceinspector.network

import com.kpn.deviceinspector.model.DeviceRegisterRequest
import com.kpn.deviceinspector.model.DeviceRegisterResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object DeviceApi {

    private const val DEVICE_API_URL =
        "https://api-staging.kpnfarmfresh.com/s/command-center/api/v1/devices?channel=AND"

    suspend fun registerDevice(token: String, deviceInfo: DeviceRegisterRequest): DeviceRegisterResponse? {
        return try {
            val response: HttpResponse = KtorClientProvider.client.post(DEVICE_API_URL) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                val filteredInfo = if (deviceInfo.device_id.isNullOrBlank()) {
                    deviceInfo.copy(device_id = null)
                } else {
                    deviceInfo
                }
                setBody(filteredInfo)
            }

            if (response.status.isSuccess()) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}