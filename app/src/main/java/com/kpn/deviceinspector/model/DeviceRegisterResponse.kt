package com.kpn.deviceinspector.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceRegisterResponse(
    @SerialName("device_id")
    val deviceId: String
)