package com.mithilakshar.mithilaksharkeyboard.utility

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResultLauncher
import com.mithilakshar.mithilaksharkeyboard.R

class Imagelyoutadder(
    private val context: Context,
    private val parentLayout: LinearLayout,
) {

    private var selectedBitmap: Bitmap? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    fun setActivityResultLauncher(launcher: ActivityResultLauncher<Intent>) {
        this.activityResultLauncher = launcher
    }

    fun showImagePickerDialog(editText: EditText) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogimageadder, null)
        val button = dialogView.findViewById<Button>(R.id.button_select_image)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Select an Image")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                selectedBitmap?.let {
                    addImageViewToLayout(it, editText)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        button.setOnClickListener {
            selectImageFromGallery()
        }

        dialog.show()
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }

    fun addImageViewToLayout(bitmap: Bitmap, editText: EditText) {
        val newImageView = ImageView(context)
        newImageView.setImageBitmap(bitmap)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        newImageView.layoutParams = layoutParams

        // Find the FrameLayout and add the ImageView
        val linearLayout: LinearLayout = parentLayout.findViewById(R.id.relative)
        linearLayout.addView(newImageView)

        // Apply ResizableTouchListener to the ImageView
        newImageView.setOnTouchListener(DragResizableTouchListener(newImageView))

        editText.requestFocus()


    }




}
