package com.mithilakshar.mithilaksharkeyboard.utility

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.mithilakshar.mithilaksharkeyboard.R

class TextViewAdder(
    private val context: Context,
    private val parentLayout: RelativeLayout
) {

    fun showTextInputDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_text_input, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText_enter_text)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Enter Text")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val enteredText = editText.text.toString()
                if (enteredText.isNotEmpty()) {
                    addTextViewToLayout(enteredText)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun addTextViewToLayout(text: String) {
        val newTextView = TextView(context)
        newTextView.text = text

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        newTextView.layoutParams = layoutParams

        newTextView.typeface=
            Typeface.create(ResourcesCompat.getFont(context, R.font.mithilakshar_dkj), Typeface.NORMAL)
        newTextView.setOnTouchListener(GestureTouchListener(context, newTextView))
        // Add the TextView to the parent layout
        parentLayout.addView(newTextView)

    }
}
