package com.mithilakshar.mithilaksharkeyboard

import android.graphics.Typeface
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputConnection
import androidx.core.content.res.ResourcesCompat

class Mithilakshar : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var keyFamily = 0
    private var firstTimeSelected = 0
    private var shiftSelected = false
    private var numberSelected = false
    private var languageSelected = false
    private var englishCapsSelected = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard, null) as CustomKeyboardView
        keyboard = Keyboard(this, R.xml.qwerty)
        keyboardView.keyboard = keyboard
        val typeface: Typeface = ResourcesCompat.getFont(this, R.font.tirhuta)!!
        ( keyboardView as CustomKeyboardView).setCustomFont(typeface)
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }


    override fun onKey(primaryCode: Int, keyCodes: IntArray) {
        val inputConnection = currentInputConnection
        switchKeyboard(inputConnection, primaryCode)
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> inputConnection.deleteSurroundingText(1, 0)
            Keyboard.KEYCODE_DONE -> inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            else -> {
                if (primaryCode !in listOf(-2, -1, -7, -6, -8)) {
                    val c = decimaltohexs().getdecimaltohex(primaryCode) ?: primaryCode.toChar().toString()
                    Log.d("text", "i: $primaryCode")
                    inputConnection.commitText(c.toString(), 1)
                }
            }
        }
    }

    override fun onPress(primaryCode: Int) {
        // Handle key press actions here
    }

    override fun onRelease(primaryCode: Int) {
        // Handle key release actions here
    }

    private fun switchKeyboard(inputConnection: InputConnection, i: Int) {
        Log.d("DEBUG", "I: $i")

        if (i !in listOf(-2, -1, -6, -7, -8)) {
            if (keyFamily != 0 && keyFamily != i && i in (keyFamily - 5)..(keyFamily + 2)) {
                if (firstTimeSelected == 0) {
                    inputConnection.deleteSurroundingText(1, 0)
                }
                firstTimeSelected++
                return
            }
            if (i in listOf(32, 4963, 4962, -4, -5)) {
                return
            }
            if (i in 35..65 || i in 4969..4988 || i == 4667 || i == 123 || i == 125) {
                // Handle specific cases
            }
        }

        Log.d("middle", "switchKeyboard: before switch")
        when (i) {
            in listOf(4677, 4672, 4673, 4674, 4675, 4676, 4678, 4679) -> {
                keyFamily = 4677
                firstTimeSelected = 0
                keyboard = Keyboard(this, R.xml.q_qwerty)
            }
            in listOf(4688, 4689, 4690, 4691, 4692, 4693, 4694, 4699) -> {
                keyFamily = 4693
                firstTimeSelected = 0
                keyboard = Keyboard(this, R.xml.qq_qwerty)
            }
            in listOf(4813, 4808, 4809, 4810, 4811, 4812, 4814) -> {
                keyFamily = 4812
                firstTimeSelected = 0
                keyboard = Keyboard(this, R.xml.w_qwerty)
            }
            // Add more cases as needed
            -1 -> {
                if (shiftSelected) {
                    keyFamily = -1
                    shiftSelected = false
                    firstTimeSelected = 0
                    keyboard = Keyboard(this, R.xml.qwerty)
                } else {
                    keyFamily = -1
                    shiftSelected = true
                    firstTimeSelected = 0
                    keyboard = Keyboard(this, R.xml.shift_qwerty)
                }
            }
            -2 -> {
                if (numberSelected) {
                    keyFamily = -2
                    numberSelected = false
                    firstTimeSelected = 0
                    keyboard = Keyboard(this, R.xml.qwerty)
                } else {
                    keyFamily = -2
                    numberSelected = true
                    firstTimeSelected = 0
                    keyboard = Keyboard(this, R.xml.number_qwerty)
                }
            }
            -6 -> {
                Log.d("TAG", "switchKeyboard: -6")
                if (languageSelected) {
                    keyFamily = -6
                    languageSelected = false
                    firstTimeSelected = 0
                    keyboard = Keyboard(this, R.xml.qwerty)
                } else {
                    keyFamily = -6
                    languageSelected = true
                    shiftSelected = false
                    firstTimeSelected = 0
                    keyboard = Keyboard(this, R.xml.english_qwerty)
                }
            }
            -7 -> {
                Log.d("TAG", "switchKeyboard: -7")
                keyFamily = -7
                englishCapsSelected = true
                firstTimeSelected = 0
                keyboard = Keyboard(this, R.xml.english_caps_qwerty)
            }
            -8 -> {
                Log.d("TAG", "switchKeyboard: -8")
                keyFamily = -8
                englishCapsSelected = true
                firstTimeSelected = 0
                keyboard = Keyboard(this, R.xml.english_qwerty)
            }
            -44 -> {
                keyFamily = -44
                keyboard = Keyboard(this, R.xml.emoji)
            }
        }
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
    }



    private fun playClick(i: Int) {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
    }

    override fun onText(text: CharSequence) {
        // Handle text input here
    }

    override fun swipeLeft() {
        // Handle swipe left action here
    }

    override fun swipeRight() {
        // Handle swipe right action here
    }

    override fun swipeDown() {
        // Handle swipe down action here
    }

    override fun swipeUp() {
        // Handle swipe up action here
    }
}
