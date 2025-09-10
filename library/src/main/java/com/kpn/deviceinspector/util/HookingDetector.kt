package com.kpn.android.deviceinspector.util

import android.util.Log
import java.io.BufferedReader
import java.io.FileReader

object HookingDetector {

    fun isHookingDetected(): Boolean {
        return hasSuspiciousClasses() || hasFridaMemoryMap()
    }

    private fun hasSuspiciousClasses(): Boolean {
        return try {
            val classes = listOf(
                "de.robv.android.xposed.XposedBridge",
                "com.saurik.substrate.MS",
                "frida.Go"
            )
            classes.any {
                try {
                    Class.forName(it)
                    DeviceInfoLogger.log("HookingDetector","Hooking class detected: $it")
                    true
                } catch (e: ClassNotFoundException) {
                    DeviceInfoLogger.recordError(e,"Hooking Class not detected")
                    false
                }
            }
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Hooking class not found")
            false
        }
    }

    private fun hasFridaMemoryMap(): Boolean {
        return try {
            val maps = StringBuilder()
            val reader = BufferedReader(FileReader("/proc/self/maps"))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                maps.append(line)
                if (line!!.contains("frida") || line!!.contains("gadget")) {
                    DeviceInfoLogger.log("HookingDetector", "Found hooking memory map: $line")
                    return true
                }
            }
            false
        } catch (e: Exception) {
            DeviceInfoLogger.recordError(e,"Error detecting Frida")
            false
        }
    }
}