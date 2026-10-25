package com.k.ffmaxpanel

import rikka.shizuku.Shizuku

object ShizukuInput {
    fun dragScreen(startX: Int, startY: Int, endX: Int, endY: Int, durationMs: Int) {
        if (Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) return
        
        val cmd = "input swipe $startX $startY $endX $endY $durationMs"
        try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", cmd), null, null)
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
