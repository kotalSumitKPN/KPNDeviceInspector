package com.kpn.deviceinspector.bridge

interface Logger {
    fun log(key: String, value: Any)
    fun recordError(error: Throwable, customMessage: String? = null)
}