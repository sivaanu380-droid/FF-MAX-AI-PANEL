package com.k.ffmaxpanel

import android.app.Service
import android.content.Intent
import android.graphics.*
import android.os.IBinder
import android.view.*

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: OverlayView

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        overlayView = OverlayView(this)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        windowManager.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::overlayView.isInitialized) windowManager.removeView(overlayView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

class OverlayView(context: android.content.Context) : View(context) {
    private val paintBox = Paint().apply { color = Color.RED; style = Paint.Style.STROKE; strokeWidth = 5f }
    private val paintText = Paint().apply { color = Color.WHITE; textSize = 50f; isAntiAlias = true }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Base drawing logic here (Grok AI-ku ithai extend panna sollunga)
        canvas.drawRect(500f, 500f, 800f, 900f, paintBox)
        canvas.drawText("Enemy", 500f, 480f, paintText)
    }
}