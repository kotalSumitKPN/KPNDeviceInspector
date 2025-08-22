package com.kpn.deviceinspector.util

import android.util.Log

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
                    Log.e("HookingDetector", "Hooking class detected: $it")
                    true
                } catch (_: ClassNotFoundException) {
                    false
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun hasFridaMemoryMap(): Boolean {
        return try {
            val maps = StringBuilder()
            val reader = java.io.BufferedReader(java.io.FileReader("/proc/self/maps"))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                maps.append(line)
                if (line!!.contains("frida") || line!!.contains("gadget")) {
                    Log.e("HookingDetector", "Found hooking memory map: $line")
                    return true
                }
            }
            false
        } catch (e: Exception) {
            false
        }
    }
}