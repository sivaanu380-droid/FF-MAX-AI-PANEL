package com.k.ffmaxpanel

import rikka.shizuku.Shizuku
import java.lang.reflect.Method

object ShizukuInput {
    fun dragScreen(startX: Int, startY: Int, endX: Int, endY: Int, durationMs: Int) {
        if (Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) return
        
        val cmd = "input swipe $startX $startY $endX $endY $durationMs"
        try {
            // Reflection vachu private method-a access panrom
            val method: Method = Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            )
            method.isAccessible = true
            val process = method.invoke(null, arrayOf("sh", "-c", cmd), null, null) as Process
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
