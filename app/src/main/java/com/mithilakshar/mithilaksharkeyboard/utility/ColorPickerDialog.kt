import android.content.Context

import android.view.LayoutInflater

import androidx.appcompat.app.AlertDialog
import com.mithilakshar.mithilaksharkeyboard.R
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar


class ColorPickerDialog(context: Context, private val listener: ColorPickerListener) : AlertDialog(context) {

    interface ColorPickerListener {
        fun onColorSelected(color: Int)
    }


    private lateinit var colorPickerView: ColorPickerView // Use TextView to show color
    private lateinit var alphaSlideBar : AlphaSlideBar // Use TextView to show color
    private lateinit var brightnessSlide : BrightnessSlideBar // Use TextView to show color
    var selectedcolor=0

    init {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.color_picker_dialog, null)
        setView(dialogView)


        colorPickerView=dialogView.findViewById(R.id.colorPickerView)
        alphaSlideBar=dialogView.findViewById(R.id.alphaSlideBar)
        brightnessSlide=dialogView.findViewById(R.id.brightnessSlide)




        colorPickerView.setColorListener(ColorListener { color, fromUser ->
            selectedcolor=color
        })


        colorPickerView.attachAlphaSlider(alphaSlideBar)
        colorPickerView.attachBrightnessSlider(brightnessSlide);


        // Create and set the dialog buttons
        setButton(BUTTON_POSITIVE, "चुनाव करू") { _, _ ->

            listener.onColorSelected(selectedcolor)
        }

        setButton(BUTTON_NEGATIVE, "रद्द करू") { dialog, _ ->
            dialog.dismiss()
        }


    }






}
