package com.dsfgland.goa.util.other

import android.content.Context
import android.provider.Settings
import java.io.File

class SecureChecker(context: Context) {
    val secured get() = isSecureADB && isSecureFS

    private val isSecureADB =
        Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED) != "1"
    private val isSecureFS = try {
        val files = listOf(
            "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
            "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"
        )
        var result = true
        files.forEach { result = result && !File(it).exists() }
        result
    } catch (e: SecurityException) {
        true
    }
}