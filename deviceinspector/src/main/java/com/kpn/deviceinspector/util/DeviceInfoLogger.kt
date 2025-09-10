package com.kpn.deviceinspector.util

import com.kpn.deviceinspector.bridge.Logger

object DeviceInfoLogger {

    private const val TAG = "Device API"
    private var logger: Logger? = null

    fun init(loggerImpl: Logger) {
        logger = loggerImpl
    }

    fun log(key: String, value: Any) {
        logger?.log("${TAG}: $key", value)
    }


    fun recordError(error: Throwable, customMessage: String? = null) {
        if (customMessage != null) {
            logger?.log("$TAG: Error", customMessage)
        }
        logger?.recordError(error, customMessage)
    }
}