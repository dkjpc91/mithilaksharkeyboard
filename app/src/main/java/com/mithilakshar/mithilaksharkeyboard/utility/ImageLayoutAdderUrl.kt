package com.mithilakshar.mithilaksharkeyboard.utility

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ImageLayoutAdderUrl(
    private val context: Context,
    private val parentLayout: RelativeLayout // Using RelativeLayout
) {

    fun addImageViewFromUrl(imageUrl: String) {
        // Create a new ImageView
        val newImageView = ImageView(context)

        // Load the image from URL into the ImageView using Glide
        Glide.with(context)
            .load(imageUrl)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    // Set the loaded image into the ImageView
                    newImageView.setImageDrawable(resource)

                    // Once the image is loaded, adjust the size based on aspect ratio
                    setImageViewSize(newImageView, resource.intrinsicWidth, resource.intrinsicHeight)

                    // Add the ImageView to the parent layout
                    parentLayout.addView(newImageView, 0) // Add at the top (index 0)

                    // Apply ResizableTouchListener to the ImageView if needed
                    newImageView.setOnTouchListener(GestureTouchListener(context, newImageView))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle load cleared if needed
                }
            })

        // Corrected: Use RelativeLayout.LayoutParams instead of FrameLayout.LayoutParams
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_TOP) // Align to the top of the parent
            addRule(RelativeLayout.CENTER_HORIZONTAL) // Center horizontally
        }
        newImageView.layoutParams = layoutParams
    }

    private fun setImageViewSize(imageView: ImageView, imageWidth: Int, imageHeight: Int) {
        val parentWidth = parentLayout.width
        val parentHeight = parentLayout.height

        // Calculate aspect ratio
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
        val paddingInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, paddingInDp.toFloat(), context.resources.displayMetrics
        ).toInt()

        imageView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

        // Corrected: Use RelativeLayout.LayoutParams
        val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        imageView.layoutParams = layoutParams
    }
}
