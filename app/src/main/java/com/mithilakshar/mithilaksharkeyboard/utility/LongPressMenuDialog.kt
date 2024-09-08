package com.mithilakshar.mithilaksharkeyboard.utility

import ColorPickerDialog
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.mithilakshar.mithilaksharkeyboard.R

class LongPressMenuDialog(
    private val context: Context,
    private val longPressedView: View,
    private val onDelete: (View) -> Unit
) {
    private var textSize = 20
    private var selectedColor = 0xFFFFFFFF.toInt()
    private var viewHeight = longPressedView.layoutParams.height
    private var viewWidth = longPressedView.layoutParams.width

    private lateinit var alertDialog: AlertDialog

    fun show() {
        longPressedView.requestFocus()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_long_press_menu, null)

        val textSizeSeekBart = dialogView.findViewById<TextView>(R.id.textSizeSeekBart)
        val textSizeSeekBar = dialogView.findViewById<SeekBar>(R.id.textSizeSeekBar)
        val textSizeSeekBarcolort = dialogView.findViewById<TextView>(R.id.textSizeSeekBarcolort)
        val textSizeSeekBarcolor = dialogView.findViewById<View>(R.id.textSizeSeekBarcolor)
        val colorPickerView = dialogView.findViewById<View>(R.id.colorPickerView)
        val deleteButton = dialogView.findViewById<TextView>(R.id.deleteButton)
        val widthSeekBar = dialogView.findViewById<SeekBar>(R.id.widthSeekBar)
        val heightSeekBar = dialogView.findViewById<SeekBar>(R.id.heightSeekBar)
        val moveToTopButton = dialogView.findViewById<TextView>(R.id.moveToTopButton)



        // Initialize Text Size SeekBar
        if (longPressedView is TextView) {
            textSizeSeekBar.visibility = View.VISIBLE
            textSizeSeekBart.visibility = View.VISIBLE
            textSizeSeekBarcolort.visibility = View.VISIBLE
            textSizeSeekBarcolor.visibility = View.VISIBLE

            textSizeSeekBar.max = 50
            textSizeSeekBar.min = 16
            textSize = longPressedView.textSize.toInt()
            textSizeSeekBar.progress = textSize

            textSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    longPressedView.textSize = progress.toFloat()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    setDialogTransparency(0.5f) // Make the background semi-transparent when touched
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    setDialogTransparency(1.0f) // Restore the background opacity
                }
            })

            textSizeSeekBarcolor.setBackgroundColor(selectedColor)
            textSizeSeekBarcolor.setOnClickListener {
                val colorPickerDialog = ColorPickerDialog(context, object : ColorPickerDialog.ColorPickerListener {
                    override fun onColorSelected(color: Int) {
                        selectedColor = color
                        colorPickerView.setBackgroundColor(selectedColor)
                        longPressedView.setTextColor(selectedColor)
                    }
                })
                colorPickerDialog.show()
            }



        } else {
            textSizeSeekBar.visibility = View.GONE
            textSizeSeekBart.visibility = View.GONE
            textSizeSeekBarcolort.visibility = View.GONE
            textSizeSeekBarcolor.visibility = View.GONE

        }


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
            alertDialog.dismiss()
        }

        // Initialize Width SeekBar
        widthSeekBar.max = 1000
        widthSeekBar.min = 200
        widthSeekBar.progress = viewWidth
        widthSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener())

        // Initialize Height SeekBar
        heightSeekBar.max = 3500
        heightSeekBar.min = 200
        heightSeekBar.progress = viewHeight
        heightSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener())

        // Initialize Move to Top Button
        moveToTopButton.setOnClickListener {
            bringViewToTop(longPressedView)
            Toast.makeText(context, "संसोधन आइटम ऊपर आबि गेल ", Toast.LENGTH_SHORT).show()
        }

        // Build and show AlertDialog
        alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("आइटम संसोधन मेनू")
            .setNegativeButton("आगू", null)
            .create()

        alertDialog.show()

        // Ensure the background is initially opaque
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.white)
    }

    private fun createSeekBarChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Logic for text size, height, width adjustments
                if (seekBar?.id == R.id.textSizeSeekBar && longPressedView is TextView) {
                    longPressedView.textSize = progress.toFloat()
                    longPressedView.requestLayout()
                }
                if (seekBar?.id == R.id.widthSeekBar) {
                    viewWidth = progress
                    longPressedView.layoutParams.width = viewWidth
                    longPressedView.requestLayout()
                }
                if (seekBar?.id == R.id.heightSeekBar) {
                    viewHeight = progress
                    longPressedView.layoutParams.height = viewHeight
                    longPressedView.requestLayout()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Make the dialog background transparent when touching the SeekBar
                setDialogTransparency(0.3f) // Set to 0.5 for semi-transparent or 0.0f for fully transparent
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Restore the dialog's background opacity when touch stops
                setDialogTransparency(1.0f)
            }
        }
    }

    private fun setDialogTransparency(transparency: Float) {
        alertDialog.window?.apply {
            attributes = attributes?.apply {
                // Set dialog transparency (1.0f = fully opaque, 0.0f = fully transparent)
                alpha = transparency
            }
        }
    }

    fun bringViewToTop(view: View) {
        val parentViewGroup = view.parent as? ViewGroup
        parentViewGroup?.let { parent ->
            parent.removeView(view)
            parent.addView(view)
            view.requestLayout()
        }
    }
}
