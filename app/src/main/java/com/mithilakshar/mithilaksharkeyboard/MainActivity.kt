package com.mithilakshar.mithilaksharkeyboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityMainBinding
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {



    private lateinit var keyboardLayout: KeyboardLayout
    private lateinit var editText: EditText
    private lateinit var textView: TextView

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find views by ID
        keyboardLayout = findViewById(R.id.mithilaksharkeyboard)
        editText = findViewById(R.id.edittext)
        textView = findViewById(R.id.textview)

        val inputConnection: InputConnection = editText.onCreateInputConnection(EditorInfo())
        keyboardLayout.setInputConnection(inputConnection)

        val typeface = ResourcesCompat.getFont(this, R.font.tirhuta)
        editText.setTypeface(typeface)

        editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val typeface = keyboardLayout.getTypeface()
                textView.typeface = typeface
                textView.text = p0
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })










    }







}