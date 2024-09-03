package com.mithilakshar.mithilaksharkeyboard.utility

import ViewDownloader
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mithilakshar.mithilaksharkeyboard.R

class CustomMenu(
    private val context: Context,
    private val main: View,
    private val fab: FloatingActionButton,
    private val linear: LinearLayout,
    private val adView: AdView

) {
    val viewDownloader = sViewDownloader(context)
    fun showDialog() {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.custommenu, null)

        // Create an AlertDialog.Builder
        val builder = AlertDialog.Builder(context)
            .setView(customView) // Set the custom layout
            .setCancelable(true)

        // Create and show the dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Find buttons in the custom layout
        val buttonOption1: Button = customView.findViewById(R.id.button_option1)
        val buttonOption2: Button = customView.findViewById(R.id.button_option2)



        // Set click listeners
        buttonOption1.setOnClickListener {
            // Capture and download the visible part of the screen
            viewDownloader.downloadViewAsImage(main, "captured_view")

            // Display a success message
            Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show()

            // Dismiss the dialog and show the second dialog
            alertDialog.dismiss()
            showSecondDialog()
        }

        buttonOption2.setOnClickListener {
            Toast.makeText(context, "Option 2 clicked", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }






    private fun showSecondDialog() {
        // Inflate the layout for the second dialog
        val inflater = LayoutInflater.from(context)
        val secondDialogView = inflater.inflate(R.layout.prompt, null)

        // Create an AlertDialog.Builder for the second dialog
        val builder = AlertDialog.Builder(context)
            .setTitle("Second Dialog")
            .setView(secondDialogView)
            .setCancelable(true)

        // Find and set up buttons in the second dialog
        val buttonOk: Button = secondDialogView.findViewById(R.id.button_ok)
        val buttonCancel: Button = secondDialogView.findViewById(R.id.button_cancel)

        buttonOk.setOnClickListener {
            Toast.makeText(context, "OK clicked", Toast.LENGTH_SHORT).show()
            // Dismiss the second dialog
            builder.create().dismiss()
        }

        buttonCancel.setOnClickListener {
            Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show()
            // Dismiss the second dialog
            builder.create().dismiss()
        }

        // Create and show the second dialog
        val secondDialog: AlertDialog = builder.create()
        secondDialog.show()
    }
}
