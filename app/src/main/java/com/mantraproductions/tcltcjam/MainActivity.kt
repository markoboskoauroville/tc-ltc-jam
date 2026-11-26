package com.mantraproductions.tcltcjam

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Calendar

class MainActivity : Activity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var clock: TextView
    private lateinit var ltcKey: Button
    private var ltc: Ltc? = null
    private var lastTimecode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        clock = TextView(this).apply {
            textSize = 54f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            setTextColor(Color.rgb(0xFF, 0xB0, 0x20))
            gravity = Gravity.CENTER
            maxLines = 1
            setAutoSizeTextTypeUniformWithConfiguration(20, 500, 1, TypedValue.COMPLEX_UNIT_SP)
        }

        ltcKey = Button(this).apply {
            text = "AUDIO LTC OUT: OFF"
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            setTextColor(Color.WHITE)
            setOnClickListener { toggleLtc() }
        }

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.BLACK)
            setPadding(32, 32, 32, 32)
            addView(clock, LinearLayout.LayoutParams(-1, 0, 1f))
            addView(ltcKey, LinearLayout.LayoutParams(-1, -2))
        })
        tick()
    }

    private fun toggleLtc() {
        if (ltc == null) {
            ltc = Ltc(System::currentTimeMillis).also { it.start() }
            ltcKey.text = "AUDIO LTC OUT: ON"
            ltcKey.setTextColor(Color.rgb(0x22, 0xC5, 0x5E))
        } else {
            ltc?.stop()
            ltc = null
            ltcKey.text = "AUDIO LTC OUT: OFF"
            ltcKey.setTextColor(Color.WHITE)
        }
    }

    private fun tick() {
        val value = timecode(System.currentTimeMillis())
        if (value != lastTimecode) {
            lastTimecode = value
            clock.text = value
        }
        handler.postDelayed(::tick, 10)
    }

    private fun timecode(ms: Long): String {
        val c = Calendar.getInstance().apply { timeInMillis = ms }
        val frame = c.get(Calendar.MILLISECOND) * 25 / 1000
        return "%02d:%02d:%02d:%02d".format(
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            c.get(Calendar.SECOND),
            frame
        )
    }

    override fun onDestroy() {
        ltc?.stop()
        ltc = null
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
