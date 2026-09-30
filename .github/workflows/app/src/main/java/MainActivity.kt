package com.zyphor.cheat

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(35, 35, 35, 35)
            setBackgroundColor(Color.BLACK)
        }

        val title = TextView(this).apply {
            text = "ZYPHOR CHEAT"
            textSize = 28f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "Fake Cheat Panel • Simulation"
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        val button = Button(this).apply {
            text = "ATIVAR PAINEL FLUTUANTE"
            setTextColor(Color.WHITE)
        }

        button.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        android.net.Uri.parse("package:$packageName")
                    )
                )
            } else {
                startService(
                    Intent(this, FloatingPanelService::class.java)
                )

                Toast.makeText(
                    this,
                    "Painel ativado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        root.addView(title)

        root.addView(
            subtitle,
            LinearLayout.LayoutParams(
                -1,
                -2
            ).apply {
                topMargin = 10
            }
        )

        root.addView(
            button,
            LinearLayout.LayoutParams(
                -1,
                -2
            ).apply {
                topMargin = 35
            }
        )

        setContentView(root)
    }
}

class FloatingPanelService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var panel: LinearLayout

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
            background = background("#111111", 22f)
        }

        val title = TextView(this).apply {
            text = "ZYPHOR CHEAT"
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(10, 5, 10, 15)
        }

        panel.addView(title)

        addToggle("Aimbot")
        addToggle("Wallhack")
        addToggle("ESP")
        addToggle("No Recoil")
        addToggle("Speed")

        val close = Button(this).apply {
            text = "FECHAR"
            setOnClickListener {
                stopSelf()
            }
        }

        panel.addView(
            close,
            LinearLayout.LayoutParams(
                -1,
                -2
            ).apply {
                topMargin = 10
            }
        )

        val params = WindowManager.LayoutParams(
            650,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER

        windowManager.addView(panel, params)
    }

    private fun addToggle(name: String) {

        val button = Button(this).apply {
            text = "$name: OFF"

            setOnClickListener {

                if (text.toString().endsWith("OFF")) {
                    text = "$name: ON"
                } else {
                    text = "$name: OFF"
                }
            }
        }

        panel.addView(
            button,
            LinearLayout.LayoutParams(
                -1,
                -2
            ).apply {
                bottomMargin = 5
            }
        )
    }

    private fun background(
        color: String,
        radius: Float
    ): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(color))
            cornerRadius = radius
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::panel.isInitialized) {
            windowManager.removeView(panel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
