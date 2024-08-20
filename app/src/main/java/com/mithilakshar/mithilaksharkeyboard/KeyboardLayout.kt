package com.mithilakshar.mithilaksharkeyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.InputConnection
import androidx.core.content.res.ResourcesCompat


class KeyboardLayout(context: Context, attrs: AttributeSet?) : KeyboardView(context, attrs)  {
    private var inputConnection: InputConnection? = null


    private var currentKey: Int = 0
    private var shiftSelected: Boolean = false
    private var numberSelected: Boolean = true
    private var languageSelected: Boolean = true
    private var englishCapsSelected: Boolean = false
    private var typeface : Typeface? =null
    private var currentLayoutResId: Int = R.xml.qwerty
    private val inflater = LayoutInflater.from(context)
    private var useCustomTypeface: Boolean = false
    private val TAG = "KeyboardLayout"
    private var paint: Paint = Paint()
    private val handler = Handler(Looper.getMainLooper())
    private var longPressRunnable: Runnable? = null
    private var isLongPressActive = false
    private var repeatDelay = 500L // Delay before repeating starts (ms)
    private var repeatInterval = 50L

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
                handleKey(primaryCode)
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

        // Only draw custom text for specific layouts
        if (currentLayoutResId == R.xml.qwerty || currentLayoutResId == R.xml.a_qwerty || currentLayoutResId == R.xml.w_qwerty) {
            keyboard?.keys?.forEach { key ->
                key?.label?.let { label ->
                    // Increase the text size for better visibility
                    paint.textSize = key.height * 0.5f // Adjust scaling factor to make text larger
                    paint.color = resources.getColor(android.R.color.black, null)

                    // Center the text (including emoji) within the key
                    val x = (key.x + key.width / 2).toFloat()
                    val y = (key.y + key.height / 2).toFloat() - (paint.descent() + paint.ascent()) / 2

                    // Draw the text (emoji)
                    canvas.drawText(label.toString(), x, y, paint)
                }
            }
        }
    }




    fun setInputConnection(ic: InputConnection?) {
        inputConnection = ic
    }



    private fun handleKey(primaryCode: Int) {

        val inputConnection = inputConnection ?: return
        updateKeyboardLayout( primaryCode)

        when (primaryCode) {

            -5 -> {
                // Handle the -5 primaryCode as delete
                Log.d("text", "Primary code -5 triggers delete")
                inputConnection.deleteSurroundingText(2, 0)
            }
            -4 -> {
                // Handle the -4 primaryCode as enter (newline)
                Log.d("text", "Primary code -4 triggers enter")
                inputConnection.commitText("\n", 1) // Inserts a newline
            }


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
                Log.d("text", "itrue: $numberSelected")
                Log.d("text", "itrue: $typeface")
                currentLayoutResId=R.xml.qwerty
                    numberSelected=false
                    R.xml.qwerty }
                else {
                Log.d("text", "ifalse: $numberSelected")
                Log.d("text", "ifalse: $typeface")
                   currentLayoutResId=R.xml.number_qwerty
                    numberSelected=true
                    R.xml.number_qwerty}

            -6 -> if (!languageSelected){

                Log.d("text", "itrue: $languageSelected")
                Log.d("text", "itrue: $typeface")

                languageSelected=true
                currentLayoutResId= R.xml.qwerty
                R.xml.qwerty
                     }

                else {

                Log.d("text", "ifalse: $languageSelected")
                Log.d("text", "ifalse: $typeface")
                currentLayoutResId= R.xml.english_qwerty
                languageSelected=false
                R.xml.english_qwerty }

            -7 ->  { currentLayoutResId= R.xml.english_caps_qwerty
                R.xml.english_caps_qwerty}
            -8 -> { currentLayoutResId= R.xml.english_qwerty
                R.xml.english_qwerty }

            -9->  if (!languageSelected){

                Log.d("text", "itrue: $languageSelected")
                Log.d("text", "itrue: $typeface")

                languageSelected=true
                currentLayoutResId= R.xml.qwerty
                R.xml.qwerty
            }

            else {

                Log.d("text", "ifalse: $languageSelected")
                Log.d("text", "ifalse: $typeface")
                currentLayoutResId= R.xml.a_qwerty
                languageSelected=false
                R.xml.a_qwerty }

            -44 ->if (!languageSelected){

                Log.d("text", "itrue: $languageSelected")
                Log.d("text", "itrue: $typeface")

                languageSelected=true
                currentLayoutResId= R.xml.qwerty
                R.xml.qwerty
            }

            else {

                Log.d("text", "ifalse: $languageSelected")
                Log.d("text", "ifalse: $typeface")
                currentLayoutResId= R.xml.english_qwerty
                languageSelected=false
                R.xml.emoji }
            else ->{typeface=typeface
                currentLayoutResId }// Default layout if keyCode does not match
        }



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
