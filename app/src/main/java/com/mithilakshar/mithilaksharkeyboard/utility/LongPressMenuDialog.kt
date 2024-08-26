package com.mithilakshar.mithilaksharkeyboard.utility

import ColorPickerDialog
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.mithilakshar.mithilaksharkeyboard.R

class LongPressMenuDialog(
    private val context: Context,
    private val longPressedView: View,
    private val onDelete: (View) -> Unit
) {
    private var textSize = 16
    private var selectedColor = 0xFF000000.toInt()
    private var viewHeight = longPressedView.layoutParams.height
    private var viewWidth = longPressedView.layoutParams.width

    fun show() {
        longPressedView.requestFocus()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_long_press_menu, null)

        val textSizeSeekBar = dialogView.findViewById<SeekBar>(R.id.textSizeSeekBar)
        val colorPickerView = dialogView.findViewById<View>(R.id.colorPickerView)
        val deleteButton = dialogView.findViewById<TextView>(R.id.deleteButton)
        val widthSeekBar = dialogView.findViewById<SeekBar>(R.id.widthSeekBar)
        val heightSeekBar = dialogView.findViewById<SeekBar>(R.id.heightSeekBar)
        val moveToTopButton = dialogView.findViewById<TextView>(R.id.moveToTopButton)

        // Initialize Text Size SeekBar
        textSizeSeekBar.max = 100
        if (longPressedView is TextView) {
            textSize = longPressedView.textSize.toInt()
            textSizeSeekBar.progress = textSize
        }
        textSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textSize = progress
                if (longPressedView is TextView) {
                    longPressedView.textSize = textSize.toFloat()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Initialize Color Picker View
        colorPickerView.setBackgroundColor(selectedColor)
        colorPickerView.setOnClickListener {
            val colorPickerDialog = ColorPickerDialog(context, object : ColorPickerDialog.ColorPickerListener {
                override fun onColorSelected(color: Int) {
                    selectedColor = color
                    colorPickerView.setBackgroundColor(selectedColor)
                    longPressedView.setBackgroundColor(selectedColor)
                }
            })
            colorPickerDialog.show()
        }

        // Initialize Delete Button
        deleteButton.setOnClickListener {
            onDelete(longPressedView)
        }

        // Initialize Width SeekBar
        widthSeekBar.max = 1000
        widthSeekBar.progress = viewWidth
        widthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewWidth = progress
                val params = longPressedView.layoutParams
                params.width = viewWidth
                longPressedView.layoutParams = params
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Initialize Height SeekBar
        heightSeekBar.max = 1000
        heightSeekBar.progress = viewHeight
        heightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewHeight = progress
                val params = longPressedView.layoutParams
                params.height = viewHeight
                longPressedView.layoutParams = params
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Initialize Move to Top Button
        moveToTopButton.setOnClickListener {
            bringViewToTop(longPressedView)

            Toast.makeText(context, "View moved to top", Toast.LENGTH_SHORT).show()
        }

        // Build and Show Dialog
        AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Customize")
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun bringViewToTop(view: View) {
        val parentViewGroup = view.parent as? ViewGroup
        parentViewGroup?.let { parent ->
            // Remove the view from its current position
            parent.removeView(view)
            // Re-add the view to the end of the parent (top of the Z-order stack)
            parent.addView(view)
            view.requestLayout()
        }
    }
}
