package com.mithilakshar.mithilaksharkeyboard

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.TextView

class BlinkingCursorTextView(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {
    private val cursor = "|"
    private var isCursorVisible = true
    private val handler = Handler(Looper.getMainLooper())
    private val cursorBlinkRate = 500L // Cursor blink rate in milliseconds

    private val blinkCursorRunnable = object : Runnable {
        override fun run() {
            toggleCursor()
            handler.postDelayed(this, cursorBlinkRate)
        }
    }

    init {
        handler.post(blinkCursorRunnable)
    }

    private fun toggleCursor() {
        isCursorVisible = !isCursorVisible
        updateTextWithCursor()
    }

    private fun updateTextWithCursor() {
        val baseText = text.toString().replace(cursor, "")
        text = if (isCursorVisible) {
            "$baseText$cursor"
        } else {
            baseText
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(blinkCursorRunnable)
    }
}
