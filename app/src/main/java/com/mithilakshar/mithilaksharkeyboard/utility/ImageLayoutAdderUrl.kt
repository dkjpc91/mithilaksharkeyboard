package com.mithilakshar.mithilaksharkeyboard.utility

import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.mithilakshar.mithilaksharkeyboard.R

class ImageLayoutAdderUrl(
    private val context: Context,
    private val parentLayout: RelativeLayout
) {

    fun addImageViewFromUrl(imageUrl: String) {
        // Create a new ImageView
        val newImageView = ImageView(context)

        // Load the image from URL into the ImageView using Glide
        Glide.with(context)
            .load(imageUrl)
            .into(newImageView)

        // Create LayoutParams with positioning
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // Center the TextView in the parent layout
            addRule(RelativeLayout.CENTER_IN_PARENT)
            setMargins(1, 1, 1, 1) // Adjust margins as needed
        }

        newImageView.layoutParams = layoutParams

        // Add ImageView to the parent layout
        parentLayout.addView(newImageView)

        // Apply ResizableTouchListener to the ImageView if needed
        newImageView.setOnTouchListener(GestureTouchListener(context, newImageView))
        setImageViewSize(newImageView)
    }

    private fun setImageViewSize(imageView: ImageView) {
        // Measure the parent layout dimensions
        val parentWidth = parentLayout.width
        val parentHeight = parentLayout.height

        // Calculate new size for ImageView (40% of parent dimensions)
        val newWidth = (parentWidth * 0.5).toInt()
        val newHeight = (parentHeight * 0.5).toInt()

        // Set the new size
        val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        imageView.layoutParams = layoutParams
    }
}
