import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable

import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.mithilakshar.mithilaksharkeyboard.R


class ColorPickerDialog(context: Context, private val listener: ColorPickerListener) : AlertDialog(context) {

    interface ColorPickerListener {
        fun onColorSelected(color: Int)
    }

    private lateinit var seekBarRed: SeekBar
    private lateinit var seekBarGreen: SeekBar
    private lateinit var seekBarBlue: SeekBar
    private lateinit var colorView: View // Use TextView to show color

    init {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.color_picker_dialog, null)
        setView(dialogView)

        seekBarRed = dialogView.findViewById(R.id.seekBarRed)
        seekBarGreen = dialogView.findViewById(R.id.seekBarGreen)
        seekBarBlue = dialogView.findViewById(R.id.seekBarBlue)
        colorView = dialogView.findViewById(R.id.colorView)

        // Set up listeners for SeekBar changes
        val updateColorView = {
            val red = seekBarRed.progress
            val green = seekBarGreen.progress
            val blue = seekBarBlue.progress
            colorView.setBackgroundColor(Color.rgb(red, green, blue))
        }

        seekBarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorView()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorView()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorView()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Create and set the dialog buttons
        setButton(BUTTON_POSITIVE, "चुनाव करू") { _, _ ->
            val red = seekBarRed.progress
            val green = seekBarGreen.progress
            val blue = seekBarBlue.progress
            val color = Color.rgb(red, green, blue)
            listener.onColorSelected(color)
        }

        setButton(BUTTON_NEGATIVE, "रद्द करू") { dialog, _ ->
            dialog.dismiss()
        }


        setSeekBarThickness(seekBarRed, 18)   // Thickness of 8dp
        setSeekBarThickness(seekBarGreen, 18) // Thickness of 8dp
        setSeekBarThickness(seekBarBlue, 18)  // Thickness of 8dp
    }



    private fun setSeekBarThickness(seekBar: SeekBar, thickness: Int) {
        val progressDrawable = seekBar.progressDrawable
        if (progressDrawable is LayerDrawable) {
            val progressLayer = progressDrawable.findDrawableByLayerId(android.R.id.progress)
            if (progressLayer is GradientDrawable) {
                progressLayer.setSize(progressLayer.intrinsicWidth, thickness)
            }
        }
    }


}
