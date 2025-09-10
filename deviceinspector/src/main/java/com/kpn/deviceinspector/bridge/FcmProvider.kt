package com.kpn.deviceinspector.bridge

interface FcmProvider {
    suspend fun getFcmToken(): String
}