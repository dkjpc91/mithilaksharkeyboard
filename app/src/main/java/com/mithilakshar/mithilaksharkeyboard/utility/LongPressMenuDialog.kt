package com.mithilakshar.mithilaksharkeyboard.utility

import ColorPickerDialog
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.mithilakshar.mithilaksharkeyboard.R
import android.widget.ImageView

class LongPressMenuDialog(
    private val context: Context,
    private val longPressedView: View,
    private val onDelete: (View) -> Unit
) {
    private var textSize = 20
    private var selectedColor = 0xFFFFFFFF.toInt()
    private var textColor = 0xFF000000.toInt() // Default text color: black
    private var viewHeight = longPressedView.layoutParams.height
    private var viewWidth = longPressedView.layoutParams.width

    private lateinit var alertDialog: AlertDialog

    fun show() {
        longPressedView.requestFocus()

        // Show Toast to indicate which view is being worked on
        Toast.makeText(context, "Working on View: ${longPressedView.javaClass.simpleName}", Toast.LENGTH_SHORT).show()

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_long_press_menu, null)

        val textSizeSeekBar = dialogView.findViewById<SeekBar>(R.id.textSizeSeekBar)
        val textSizeTextView = dialogView.findViewById<TextView>(R.id.textSizeTextView)
        val textColorButton = dialogView.findViewById<View>(R.id.textColorButton)
        val colorPickerView = dialogView.findViewById<View>(R.id.colorPickerView)
        val deleteButton = dialogView.findViewById<TextView>(R.id.deleteButton)
        val widthSeekBar = dialogView.findViewById<SeekBar>(R.id.widthSeekBar)
        val heightSeekBar = dialogView.findViewById<SeekBar>(R.id.heightSeekBar)
        val moveToTopButton = dialogView.findViewById<TextView>(R.id.moveToTopButton)
        val editTextButton = dialogView.findViewById<TextView>(R.id.editTextButton)

        if (longPressedView is TextView) {
            setupTextViewControls(textSizeSeekBar, textSizeTextView, textColorButton, editTextButton)
        } else {
            hideTextViewControls(textSizeSeekBar, textSizeTextView, textColorButton, editTextButton)
        }

        setupColorPicker(colorPickerView)
        setupDeleteButton(deleteButton)
        setupSeekBars(widthSeekBar, heightSeekBar)
        setupMoveToTopButton(moveToTopButton)

        alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("आइटम संसोधन मेनू")
            .setNegativeButton("आगू", null)
            .create()

        alertDialog.show()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.white)
    }

    private fun setupTextViewControls(
        textSizeSeekBar: SeekBar,
        textSizeTextView: TextView,
        textColorButton: View,
        editTextButton: TextView
    ) {
        textSizeSeekBar.visibility = View.VISIBLE
        textSizeTextView.visibility = View.VISIBLE
        textColorButton.visibility = View.VISIBLE
        editTextButton.visibility = View.VISIBLE

        textSizeSeekBar.max = 50
        textSizeSeekBar.min = 16
        textSize = (longPressedView as TextView).textSize.toInt()
        textSizeSeekBar.progress = textSize

        textSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                longPressedView.textSize = progress.toFloat()
                textSizeTextView.text = "Text Size: ${progress}sp"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                setDialogTransparency(0.5f)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setDialogTransparency(1.0f)
            }
        })

        textColorButton.setBackgroundColor(textColor)
        textColorButton.setOnClickListener {
            showColorPickerForTextColor(textColorButton)
        }

        editTextButton.setOnClickListener {
            showEditTextDialog(longPressedView)
        }
    }

    private fun hideTextViewControls(
        textSizeSeekBar: SeekBar,
        textSizeTextView: TextView,
        textColorButton: View,
        editTextButton: TextView
    ) {
        textSizeSeekBar.visibility = View.GONE
        textSizeTextView.visibility = View.GONE
        textColorButton.visibility = View.GONE
        editTextButton.visibility = View.GONE
    }

    private fun showColorPickerForTextColor(textColorButton: View) {
        val colorPickerDialog = ColorPickerDialog(context, object : ColorPickerDialog.ColorPickerListener {
            override fun onColorSelected(color: Int) {
                textColor = color
                (longPressedView as TextView).setTextColor(textColor)
                textColorButton.setBackgroundColor(textColor)
            }
        })
        colorPickerDialog.show()
    }

    private fun setupColorPicker(colorPickerView: View) {
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
    }

    private fun setupDeleteButton(deleteButton: TextView) {
        deleteButton.setOnClickListener {
            onDelete(longPressedView)
            alertDialog.dismiss()
        }
    }

    private fun setupSeekBars(widthSeekBar: SeekBar, heightSeekBar: SeekBar) {
        widthSeekBar.max = 1000
        widthSeekBar.min = 200
        widthSeekBar.progress = viewWidth
        widthSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener())

        heightSeekBar.max = 3500
        heightSeekBar.min = 200
        heightSeekBar.progress = viewHeight
        heightSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener())
    }

    private fun setupMoveToTopButton(moveToTopButton: TextView) {
        moveToTopButton.setOnClickListener {
            bringViewToTop(longPressedView)
            Toast.makeText(context, "संसोधन आइटम ऊपर आबि गेल ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditTextDialog(textView: TextView) {
        val editTextDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null)
        val editText = editTextDialogView.findViewById<EditText>(R.id.editText)
        editText.setText(textView.text)

        val editTextDialog = AlertDialog.Builder(context)
            .setView(editTextDialogView)
            .setTitle("Edit Text")
            .setPositiveButton("Apply") { _, _ ->
                textView.text = editText.text
            }
            .setNegativeButton("Cancel", null)
            .create()

        editTextDialog.show()
    }

    private fun createSeekBarChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (seekBar?.id) {
                    R.id.textSizeSeekBar -> {
                        if (longPressedView is TextView) {
                            longPressedView.textSize = progress.toFloat()
                            longPressedView.requestLayout()
                        }
                    }
                    R.id.widthSeekBar -> {
                        viewWidth = progress
                        longPressedView.layoutParams.width = viewWidth
                        longPressedView.requestLayout()
                    }
                    R.id.heightSeekBar -> {
                        viewHeight = progress
                        longPressedView.layoutParams.height = viewHeight
                        longPressedView.requestLayout()
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                setDialogTransparency(0.3f)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setDialogTransparency(1.0f)
            }
        }
    }

    private fun setDialogTransparency(transparency: Float) {
        alertDialog.window?.apply {
            attributes = attributes?.apply {
                alpha = transparency
            }
        }
    }

    private fun bringViewToTop(view: View) {
        val parentViewGroup = view.parent as? ViewGroup
        parentViewGroup?.let { parent ->
            parent.removeView(view)
            parent.addView(view)
            view.requestLayout()
        }
    }
}
