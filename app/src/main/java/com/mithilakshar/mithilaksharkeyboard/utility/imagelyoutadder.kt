package com.mithilakshar.mithilaksharkeyboard.utility

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResultLauncher
import com.mithilakshar.mithilaksharkeyboard.R

class Imagelyoutadder(
    private val context: Context,
    private val parentLayout: RelativeLayout, // Keep as RelativeLayout
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val onImagePicked: (Uri) -> Unit
) {

    private var selectedBitmap: Bitmap? = null

    fun showImagePickerDialog(frameLayout: RelativeLayout) { // No need for FrameLayout parameter
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogimageadder, null)
        val button = dialogView.findViewById<Button>(R.id.selectimagebutton)

        val dialog = AlertDialog.Builder(context)
            .setTitle("मनपसंद फोटो के चुनाव करू")
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

    fun addImageViewToLayout(bitmap: Bitmap, relative: RelativeLayout) { // Removed extra parameter
        val newImageView = ImageView(context)
        newImageView.setImageBitmap(bitmap)

        // Use RelativeLayout.LayoutParams instead of FrameLayout.LayoutParams
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // Align image to the top-center of the parent RelativeLayout
            addRule(RelativeLayout.ALIGN_PARENT_TOP)
            addRule(RelativeLayout.CENTER_HORIZONTAL)
        }

        newImageView.layoutParams = layoutParams

        // Add ImageView to parent RelativeLayout
        parentLayout.addView(newImageView)

        setImageViewSize(newImageView, bitmap.width, bitmap.height)

        // Apply touch listener for resizing/moving
        newImageView.setOnTouchListener(GestureTouchListener(context, newImageView))
    }

    private fun setImageViewSize(imageView: ImageView, imageWidth: Int, imageHeight: Int) {
        val parentWidth = parentLayout.width
        val parentHeight = parentLayout.height

        // Calculate aspect ratio
        val aspectRatio = imageWidth.toFloat() / imageHeight
        val newWidth: Int
        val newHeight: Int

        if (parentWidth < parentHeight * aspectRatio) {
            // Set width to 60% of the parent and adjust height accordingly
            newWidth = (parentWidth * 0.6).toInt()
            newHeight = (newWidth / aspectRatio).toInt()
        } else {
            // Set height to 60% of the parent and adjust width accordingly
            newHeight = (parentHeight * 0.6).toInt()
            newWidth = (newHeight * aspectRatio).toInt()
        }

        val paddingInDp = 2
        val paddingInPx = (context.resources.displayMetrics.density * paddingInDp).toInt()

        imageView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

        // Use RelativeLayout.LayoutParams instead of FrameLayout.LayoutParams
        val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        imageView.layoutParams = layoutParams
    }
}
