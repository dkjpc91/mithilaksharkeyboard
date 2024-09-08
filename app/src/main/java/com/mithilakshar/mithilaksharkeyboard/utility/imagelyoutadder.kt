package com.mithilakshar.mithilaksharkeyboard.utility

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.utility.ImagePicker.Companion.REQUEST_CODE

class Imagelyoutadder(
    private val context: Context,
    private val parentLayout: RelativeLayout,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val onImagePicked: (Uri) -> Unit

) {

    private var selectedBitmap: Bitmap? = null




    fun showImagePickerDialog(relativelayout: RelativeLayout) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogimageadder, null)
        val button = dialogView.findViewById<Button>(R.id.selectimagebutton)

        val dialog = AlertDialog.Builder(context)
            .setTitle("मनपसंद फोटो के चुनाव करू ")
            .setView(dialogView)

            .setNegativeButton("कैंसिल करू", null)
            .create()

        button.setOnClickListener {
            selectImageFromGallery()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }




    fun addImageViewToLayout(bitmap: Bitmap, relativelayout: RelativeLayout) {
        val newImageView = ImageView(context)
        newImageView.setImageBitmap(bitmap)

        // Create LayoutParams with positioning below another view
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // Center the TextView in the parent layout
            addRule(RelativeLayout.CENTER_IN_PARENT)
            // Adjust margins as needed
        }


        newImageView.layoutParams = layoutParams

        // Find the RelativeLayout and add the ImageView
        val relativeLayout: RelativeLayout = parentLayout.findViewById(R.id.relative)
        relativeLayout.addView(newImageView)
        setImageViewSize(newImageView, bitmap.width, bitmap.height)
        // Apply ResizableTouchListener to the ImageView
        newImageView.setOnTouchListener(GestureTouchListener(context, newImageView))


    }

    private fun setImageViewSize(imageView: ImageView, imageWidth: Int, imageHeight: Int) {
        val parentWidth = parentLayout.width
        val parentHeight = parentLayout.height

        val aspectRatio = imageWidth.toFloat() / imageHeight
        val newWidth: Int
        val newHeight: Int

        if (parentWidth < parentHeight * aspectRatio) {
            newWidth = (parentWidth * 0.6).toInt()
            newHeight = (newWidth / aspectRatio).toInt()
        } else {
            newHeight = (parentHeight * 0.6).toInt()
            newWidth = (newHeight * aspectRatio).toInt()
        }

        val paddingInDp = 2
        val paddingInPx = (context.resources.displayMetrics.density * paddingInDp).toInt()

        imageView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

        val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        imageView.layoutParams = layoutParams
    }

}








