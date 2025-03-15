package com.mithilakshar.mithilaksharkeyboard.utility

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.mithilakshar.mithilaksharkeyboard.R

class TextViewAdder(
    private val context: Context,
    private val parentLayout: RelativeLayout, // Changed to FrameLayout
    private val applyCustomFont: Boolean
) {

    fun showTextInputDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_text_input, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText_enter_text)
        val text_heading = dialogView.findViewById<TextView>(R.id.text_heading)
        val text_description = dialogView.findViewById<TextView>(R.id.text_description)

        if (!applyCustomFont) {
            text_description.text = "देवनागरी या अंग्रेजी अन्य लिपि में लिखू "
            text_heading.text = "देवनागरी "
        }

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("आगू") { _, _ ->
                val enteredText = editText.text.toString()
                if (enteredText.isNotEmpty()) {
                    addTextViewToLayout(enteredText)
                }
            }
            .setNegativeButton("कैंसिल करू", null)
            .create()

        dialog.show()
    }

    private fun addTextViewToLayout(text: String) {
        val newTextView = TextView(context)
        newTextView.text = text
        newTextView.textSize = 24f // Set text size to 24
        newTextView.gravity = android.view.Gravity.CENTER

        // Convert 100 dp to pixels for width
        val displayMetrics = context.resources.displayMetrics
        val widthInPixels = (displayMetrics.widthPixels - dpToPx(100, displayMetrics)).toInt()

        val layoutParams = FrameLayout.LayoutParams(
            widthInPixels,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // Center the TextView in the parent layout
            gravity = android.view.Gravity.TOP or android.view.Gravity.CENTER_HORIZONTAL
        }

        newTextView.layoutParams = layoutParams

        if (applyCustomFont) {
            // Apply the custom font if the boolean flag is true
            newTextView.typeface =
                Typeface.create(ResourcesCompat.getFont(context, R.font.mithilakshar_dkj), Typeface.NORMAL)
        }

        newTextView.setOnTouchListener(GestureTouchListener(context, newTextView))

        // Add the TextView to the parent layout at the top (index 0)
        parentLayout.addView(newTextView, 0) // Adding at the top

    }

    // Helper function to convert dp to pixels
    private fun dpToPx(dp: Int, displayMetrics: android.util.DisplayMetrics): Float {
        return dp * (displayMetrics.densityDpi / 160f)
    }
}
