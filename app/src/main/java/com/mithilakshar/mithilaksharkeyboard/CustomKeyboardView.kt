package com.mithilakshar.mithilaksharkeyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet

class CustomKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : KeyboardView(context, attrs, defStyle) {

    private var customTypeface: Typeface = Typeface.DEFAULT
    private val paint = Paint()

    init {
       // paint.textSize = 40f // Default text size
       // paint.color = android.graphics.Color.BLACK
        //paint.isAntiAlias = true

        // Load the keyboard layout
        val keyboard = Keyboard(context, R.xml.qwerty) // Load keyboard XML
        setKeyboard(keyboard)
    }

    fun setCustomFont(typeface: Typeface) {
        customTypeface = typeface
        paint.typeface = customTypeface
        invalidate() // Redraw the view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.typeface = customTypeface
        val keys = keyboard?.keys ?: return
        for (key in keys) {
            val label = key.label?.toString() ?: continue
            val x = key.x + (key.width - paint.measureText(label)) / 2
            val y = key.y + (key.height + paint.textSize) / 2
            canvas.drawText(label, x, y, paint)
        }
    }
}
