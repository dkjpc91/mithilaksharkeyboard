package com.mithilakshar.mithilaksharkeyboard.utility

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import java.io.OutputStream

class ViewDownloader(private val context: Context) {

    fun downloadViewAsImage(view: View, fileName: String, viewsToRemove: List<View> = emptyList()) {
        val parentView = view.parent as? ViewGroup ?: return

        // Measure the parent view to get its initial height
        val originalHeight = parentView.height

        // Save the original layout parameters of the views to be removed
        val originalParams = viewsToRemove.map { it.layoutParams }

        try {
            // Temporarily hide and adjust the layout parameters of the specified views
            viewsToRemove.forEach { viewToRemove ->
                viewToRemove.visibility = View.GONE
            }

            // Collapse the space occupied by the hidden views
            parentView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            parentView.requestLayout()

            // Convert the view to a Bitmap
            val bitmap = getBitmapFromView(view)

            // Restore the original layout parameters and visibility of the hidden views
            viewsToRemove.forEachIndexed { index, viewToRestore ->
                viewToRestore.visibility = View.VISIBLE
                viewToRestore.layoutParams = originalParams[index]
            }

            // Restore the original height of the parent view
            parentView.layoutParams.height = originalHeight
            parentView.requestLayout()

            // Save the bitmap to the Pictures directory
            saveBitmapToPictures(bitmap, fileName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun getBitmapFromView(view: View): Bitmap {
        // Enable drawing cache
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()

        // Create the bitmap from the view's drawing cache
        val bitmap = Bitmap.createBitmap(view.drawingCache)

        // Disable drawing cache
        view.isDrawingCacheEnabled = false

        return bitmap
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
