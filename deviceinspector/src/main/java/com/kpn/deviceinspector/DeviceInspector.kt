package com.kpn.deviceinspector

import android.content.Context
import android.os.Build
import com.kpn.deviceinspector.util.AppSetIdProvider
import com.kpn.deviceinspector.util.BatteryUtil
import com.kpn.deviceinspector.util.CarrierUtil
import com.kpn.deviceinspector.util.CloneAppDetector
import com.kpn.deviceinspector.util.DeveloperSettingsUtil
import com.kpn.deviceinspector.util.DeviceMaskingDetector
import com.kpn.deviceinspector.util.DeviceTypeUtil
import com.kpn.deviceinspector.util.EmulatorChecker
import com.kpn.deviceinspector.util.FactoryResetDetector
import com.kpn.deviceinspector.util.GpsSpooferDetector
import com.kpn.deviceinspector.util.HookingDetector
import com.kpn.deviceinspector.util.NetworkUtil
import com.kpn.deviceinspector.util.PayloadTamperDetector
import com.kpn.deviceinspector.util.RootDetector
import com.kpn.deviceinspector.util.ScreenSharingDetector
import com.kpn.deviceinspector.util.SecondaryUserDetector
import com.kpn.deviceinspector.util.TamperCheckUtil
import com.kpn.deviceinspector.util.VirtualOsDetector
import com.kpn.deviceinspector.util.VpnSpooferDetector
import com.kpn.deviceinspector.util.AutoClickerDetector
import com.kpn.store.deviceinspector.model.DeviceRegisterRequest
import java.util.Locale

object DeviceInspector {

    suspend fun collect(
        context: Context,
        anonymousUserId: String,
        userId: String,
        deviceId: String?,
        fcmId: String,
        createdByApp: String,
        expectedSignatureSha256: String
    ): DeviceRegisterRequest {

        val metrics = context.resources.displayMetrics
        val screenSize = "${metrics.widthPixels / metrics.density} x ${metrics.heightPixels / metrics.density}"

        return DeviceRegisterRequest(
            anonymous_user_id = anonymousUserId,
            user_id = userId,
            device_id = deviceId ?: "",
            customer_id = userId,
            platform = "AND",
            fcm_id = fcmId,
            created_by_app = createdByApp,
            created_by_user = userId,
            brand = Build.BRAND,
            model = Build.MODEL,
            device_type = DeviceTypeUtil.getDeviceType(context),
            manufacturer = Build.MANUFACTURER,
            hardware = Build.HARDWARE,
            board = Build.BOARD,
            android_id = AppSetIdProvider.getDeveloperScopedIdOrPlaceholder(context),
            display_type = Build.DISPLAY,
            screen_size = screenSize,
            app_bundle_name = context.packageName,
            os_version = "Android ${Build.VERSION.RELEASE}",
            security_patch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Build.VERSION.SECURITY_PATCH ?: ""
            } else {
                "unknown"
            },
            carrier = CarrierUtil.getCarrierName(context),
            android_build = Build.ID,
            user_agent = System.getProperty("http.agent") ?: "",
            java_version = System.getProperty("java.version") ?: "",
            user_language = Locale.getDefault().language,
            user_region = Locale.getDefault().country,
            wifi_enabled = NetworkUtil.isWifiEnabled(context),
            wifi_ssid = NetworkUtil.getWifiSSID(context),
            battery_level = BatteryUtil.getBatteryLevel(context),
            is_app_tampered = TamperCheckUtil.isAppTampered(context, expectedSignatureSha256),
            is_auto_clicker_enabled = AutoClickerDetector.isAutoClickerInstalled(context),
            is_developer_option_enabled = DeveloperSettingsUtil.isDeveloperOptionsEnabled(context),
            is_debugging_enabled = DeveloperSettingsUtil.isDebuggingEnabled(context),
            is_hooking = HookingDetector.isHookingDetected(),
            is_device_masked = DeviceMaskingDetector.isDeviceMasked(),
            is_emulator = EmulatorChecker.isEmulator(),
            is_jail_broken = RootDetector.isDeviceRooted(),
            is_running_clone_app = CloneAppDetector.isAppCloned(context),
            is_running_gps_spoofer = GpsSpooferDetector.isMockLocationEnabled(context),
            is_running_screen_sharing = ScreenSharingDetector.isScreenSharingActive(context),
            is_running_vpn_spoofers = VpnSpooferDetector.isVpnActive(context),
            is_secondary_user = SecondaryUserDetector.isSecondaryUser(),
            is_suspicious_factory_reset = FactoryResetDetector.isSuspiciousFactoryReset(context), // Just checks if device restarted and if less than 24 hours takes as recent
            is_virtual_os = VirtualOsDetector.isRunningInVirtualOs(),
            is_request_payload_tampered = PayloadTamperDetector.isPayloadTampered(),
            session_by_app = createdByApp,
            session_by_user = userId
        )
    }
}