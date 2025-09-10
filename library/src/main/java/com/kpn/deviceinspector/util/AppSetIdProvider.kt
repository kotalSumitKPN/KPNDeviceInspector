package com.kpn.android.deviceinspector.util

import android.content.Context
import android.util.Log
import com.google.android.gms.appset.AppSet
import com.google.android.gms.appset.AppSetIdInfo
import kotlinx.coroutines.tasks.await

object AppSetIdProvider {
    private const val TAG = "AppSetIdProvider"
    private const val PLACEHOLDER = "unrecognized"

    /**
     * Returns developer-scoped App Set ID if available; otherwise returns "unrecognized".
     * Never throws.
     */
    suspend fun getDeveloperScopedIdOrPlaceholder(context: Context): String {
        return try {
            val info = AppSet.getClient(context).appSetIdInfo.await()
            DeviceInfoLogger.log(TAG, "AppSet scope=${info.scope} (2=DEVELOPER, 1=APP)")
            if (info.scope == AppSetIdInfo.SCOPE_DEVELOPER) info.id else PLACEHOLDER
        } catch (t: Throwable) {
            DeviceInfoLogger.recordError(
                t,
                "AppSet unavailable"
            )
            PLACEHOLDER
        }
    }
}