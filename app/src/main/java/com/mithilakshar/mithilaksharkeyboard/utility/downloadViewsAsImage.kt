package com.mithilakshar.mithilaksharkeyboard.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class specificViewDownloader {

    fun downloadViewsAsImage(context: Context, views: List<View>, fileName: String): Boolean {
        // Capture the combined bitmap of the specified views
        val bitmap = getCombinedBitmapFromViews(views)

        // Save the Bitmap to the Download directory
        return saveBitmapToDownloadDirectory(context, bitmap, fileName)
    }

    private fun getCombinedBitmapFromViews(views: List<View>): Bitmap {
        // Determine the total width and height needed for the combined bitmap
        var width = 0
        var height = 0
        for (view in views) {
            val rect = android.graphics.Rect()
            view.getGlobalVisibleRect(rect)
            width = maxOf(width, rect.right)
            height = maxOf(height, rect.bottom)
        }

        // Create a Bitmap with the combined dimensions
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw each view onto the bitmap
        for (view in views) {
            val rect = android.graphics.Rect()
            view.getGlobalVisibleRect(rect)
            canvas.translate(-rect.left.toFloat(), -rect.top.toFloat())
            view.draw(canvas)
            canvas.translate(rect.left.toFloat(), rect.top.toFloat())
        }

        return bitmap
    }

    private fun saveBitmapToDownloadDirectory(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        // Get the Download directory
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs()
        }

        // Create the file in the Download directory
        val file = File(downloadDirectory, "$fileName.png")

        var fos: FileOutputStream? = null
        return try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            true
        } catch (e: IOException) {
            Log.e("ViewDownloader", "Error saving bitmap", e)
            false
        } finally {
            fos?.close()
        }
    }
}
