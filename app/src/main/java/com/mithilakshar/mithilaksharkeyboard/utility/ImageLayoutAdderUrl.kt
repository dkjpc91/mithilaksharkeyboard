import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.doOnPreDraw
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.Glide
import com.mithilakshar.mithilaksharkeyboard.utility.GestureTouchListener

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
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    // Set the loaded image into the ImageView
                    newImageView.setImageDrawable(resource)

                    // Once the image is loaded, adjust the size based on aspect ratio
                    setImageViewSize(newImageView, resource.intrinsicWidth, resource.intrinsicHeight)

                    // Add the ImageView to the parent layout
                    parentLayout.addView(newImageView)

                    // Apply ResizableTouchListener to the ImageView if needed
                    newImageView.setOnTouchListener(GestureTouchListener(context, newImageView))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle load cleared if needed
                }
            })

        // Create LayoutParams with positioning
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // Center the ImageView in the parent layout
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }
        newImageView.layoutParams = layoutParams
    }

    private fun setImageViewSize(imageView: ImageView, imageWidth: Int, imageHeight: Int) {
        // Measure the parent layout dimensions
        val parentWidth = parentLayout.width
        val parentHeight = parentLayout.height

        // Calculate the scaling factor while maintaining aspect ratio
        val aspectRatio = imageWidth.toFloat() / imageHeight
        val newWidth: Int
        val newHeight: Int

        if (parentWidth < parentHeight * aspectRatio) {
            // Set width to 60% of parent and adjust height based on aspect ratio
            newWidth = (parentWidth * 0.6).toInt()
            newHeight = (newWidth / aspectRatio).toInt()
        } else {
            // Set height to 60% of parent and adjust width based on aspect ratio
            newHeight = (parentHeight * 0.6).toInt()
            newWidth = (newHeight * aspectRatio).toInt()
        }

        val paddingInDp = 2
        val paddingInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, paddingInDp.toFloat(), context.resources.displayMetrics
        ).toInt()

        imageView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

        // Increase the layout size by 2dp (paddingInPx) on all sides
        val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        imageView.layoutParams = layoutParams


    }
}
