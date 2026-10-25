package com.k.ffmaxpanel

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_CAPTURE = 100
    private val REQUEST_CODE_OVERLAY = 101
    private val REQUEST_CODE_SHIZUKU = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val btnStart = Button(this)
        btnStart.text = "START PANEL"
        layout.addView(btnStart)
        setContentView(layout)

        btnStart.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        if (!Settings.canDrawOverlays(this)) {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), REQUEST_CODE_OVERLAY)
            Toast.makeText(this, "Grant Overlay permission", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            if (Shizuku.isPreV11() || Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(REQUEST_CODE_SHIZUKU)
                return
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Shizuku not running!", Toast.LENGTH_SHORT).show()
        }

        startScreenCapture()
    }

    private fun startScreenCapture() {
        val mpm = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mpm.createScreenCaptureIntent(), REQUEST_CODE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, ScreenCaptureService::class.java).apply {
                putExtra("RESULT_CODE", resultCode)
                putExtra("DATA", data)
            }
            startForegroundService(intent)
            startService(Intent(this, OverlayService::class.java))
            Toast.makeText(this, "Panel Started!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == REQUEST_CODE_SHIZUKU) {
            if (Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                startScreenCapture()
            }
        }
    }
}
