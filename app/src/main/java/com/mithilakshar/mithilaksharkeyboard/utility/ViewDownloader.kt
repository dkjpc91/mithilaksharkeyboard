import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import java.io.OutputStream

class ViewDownloader(private val context: Context) {

    fun downloadViewAsImage(view: View, fileName: String, viewsToRemove: List<View> = emptyList()) {
        val parentView = view.parent as? ViewGroup ?: return

        // Save the original layout parameters and background of the views to be removed
        val originalParams = viewsToRemove.map { it.layoutParams }
        val originalBackgrounds = viewsToRemove.map { it.background }

        try {
            // Temporarily hide and adjust the layout parameters of the specified views
            viewsToRemove.forEachIndexed { index, viewToRemove ->
                // Set the background to transparent
                viewToRemove.setBackgroundColor(Color.TRANSPARENT)
                viewToRemove.visibility = View.GONE
            }

            // Collapse the space occupied by the hidden views
            parentView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            parentView.requestLayout()

            // Force the layout to redraw itself before capturing the bitmap
            parentView.invalidate()
            parentView.postDelayed({
                // Create a bitmap with the size of the parent view
                val bitmap = Bitmap.createBitmap(parentView.width, parentView.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                parentView.draw(canvas)

                // Restore the original layout parameters and visibility of the hidden views
                viewsToRemove.forEachIndexed { index, viewToRestore ->
                    viewToRestore.visibility = View.VISIBLE
                    viewToRestore.background = originalBackgrounds[index]
                    viewToRestore.layoutParams = originalParams[index]
                }

                // Restore the original height of the parent view
                parentView.layoutParams.height = parentView.height
                parentView.requestLayout()

                // Save the bitmap to the Pictures directory
                saveBitmapToPictures(bitmap, fileName)
            }, 100) // Delay slightly to ensure the layout is redrawn
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveBitmapToPictures(bitmap: Bitmap, fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/YourSubdirectory")
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            } finally {
                outputStream?.close()
            }
        }
    }
}
