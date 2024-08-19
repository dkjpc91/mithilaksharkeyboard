package com.mithilakshar.mithilaksharkeyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent

import android.view.LayoutInflater
import android.view.inputmethod.InputConnection
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat


class KeyboardLayout(context: Context, attrs: AttributeSet?) : KeyboardView(context, attrs)  {
    private var inputConnection: InputConnection? = null


    private var currentKey: Int = 0
    private var shiftSelected: Boolean = false
    private var numberSelected: Boolean = false
    private var languageSelected: Boolean = false
    private var englishCapsSelected: Boolean = false
    private var typeface : Typeface? =null
    private var currentLayoutResId: Int = R.xml.qwerty
    private val inflater = LayoutInflater.from(context)
    private var useCustomTypeface: Boolean = false
    private val TAG = "KeyboardLayout"
    private var paint: Paint = Paint()
    init {
         typeface = ResourcesCompat.getFont(context, R.font.tirhuta)
        paint.typeface = typeface
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER



        // Inflate and set up the keyboard
       var keyboard = Keyboard(context, R.xml.qwerty)
        setKeyboard(keyboard)
        setOnKeyboardActionListener(object : KeyboardView.OnKeyboardActionListener {
            // Implement necessary methods here


            override fun onKey(primaryCode: Int, p1: IntArray?) {

                currentKey = primaryCode
                handleKey(primaryCode,context)
            }


            override fun onPress(p0: Int) {

            }

            override fun onRelease(p0: Int) {

            }


            override fun onText(p0: CharSequence?) {

            }

            override fun swipeLeft() {

            }

            override fun swipeRight() {

            }

            override fun swipeDown() {

            }

            override fun swipeUp() {

            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (currentLayoutResId==R.xml.qwerty){
            keyboard?.keys?.forEach { key ->
                key?.label?.let { label ->
                    paint.textSize = key.height * 0.5f
                    paint.color = resources.getColor(android.R.color.black, null)
                    val x = (key.x + key.width / 2).toFloat()
                    val y = (key.y + key.height / 2).toFloat() + paint.textSize / 3
                    canvas.drawText(label.toString(), x, y, paint)
                }
            }

        }
        // Draw each key with custom typeface

    }

     fun run(canvas: Canvas) {
        if (true) {
            // Custom drawing for the Tirhuta script or specific layout
            keyboard?.keys?.forEach { key ->
                key?.label?.let { label ->
                    paint.textSize = key.height * 0.5f
                    paint.color = resources.getColor(android.R.color.black, null)
                    val x = (key.x + key.width / 2).toFloat()
                    val y = (key.y + key.height / 2).toFloat() + paint.textSize / 3
                    canvas.drawText(label.toString(), x, y, paint)
                }
            }
        } else {
            // For other layouts, use the default drawing behavior
            super.draw(canvas)
        }
    }


    fun setInputConnection(ic: InputConnection?) {
        inputConnection = ic
    }



    private fun handleKey(primaryCode: Int,context: Context) {

        val inputConnection = inputConnection ?: return
        updateKeyboardLayout( primaryCode)

        when (primaryCode) {

            Keyboard.KEYCODE_DELETE -> inputConnection.deleteSurroundingText(1, 0)
            Keyboard.KEYCODE_DONE -> inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))

            else -> {
                if (primaryCode !in listOf(-2, -1, -7, -6, -8)) {
                    // Convert primaryCode to character
                    val c = decimaltohexs().getdecimaltohexsurrogate(primaryCode) ?: primaryCode.toChar().toString()
                    Log.d("text", "i: $primaryCode")
                    inputConnection.commitText(c, 1)
                }
            }
        }

    }





    private fun updateKeyboardLayout(keyCode: Int) {
        Log.d("text", "i: $keyCode")

        // Determine which layout to use based on keyCode
        val layoutResId = when (keyCode) {
            in listOf(4677, 4672, 4673, 4674, 4675, 4676, 4678, 4679) -> R.xml.q_qwerty
            in listOf(4688, 4689, 4690, 4691, 4692, 4693, 4694, 4699) -> R.xml.qq_qwerty
            in listOf(4813, 4808, 4809, 4810, 4811, 4812, 4814) -> R.xml.w_qwerty
            -1 -> if (shiftSelected) R.xml.qwerty else R.xml.shift_qwerty

            -2 -> if (numberSelected){
                currentLayoutResId=R.xml.qwerty
                    numberSelected=false
                    R.xml.qwerty }
                else {
                    currentLayoutResId=R.xml.number_qwerty
                    numberSelected=true
                    R.xml.number_qwerty}

            -6 -> if (languageSelected) R.xml.qwerty else R.xml.english_qwerty
            -7 -> R.xml.english_caps_qwerty
            -8 -> R.xml.english_qwerty
            -44 -> R.xml.emoji
            else -> R.xml.qwerty // Default layout if keyCode does not match
        }


        useCustomTypeface = layoutResId == R.xml.q_qwerty
        // Instantiate the new Keyboard object
        val newKeyboard = Keyboard(context, layoutResId)
        paint.typeface = typeface
        // Set the new keyboard to the KeyboardView
        this.keyboard = newKeyboard
        this.invalidate() // Refresh the view to apply changes
    }

    fun getTypeface(): Typeface? {
        return paint.typeface
    }




}
