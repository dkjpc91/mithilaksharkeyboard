package com.mithilakshar.mithilaksharkeyboard.utility


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class sViewDownloader(private val context: Context) {


        fun downloadViewAsImage(view: View, fileName: String, context: Context) {
            try {
                // Create a bitmap with the size of the view
                val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                view.draw(canvas)

                // Save the bitmap to the Pictures directory
                saveBitmapToPictures(bitmap, fileName, context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        private fun saveBitmapToPictures(bitmap: Bitmap, fileName: String, context: Context) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/YourSubdirectory"
                )
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
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

        fun shareBitmapAsImage(view: View, context: Context) {
            val bitmap = viewToBitmap(view)
            val contentUri = Uri.parse("content://${context.packageName}.bitmapprovider")
            BitmapContentProvider.setBitmap(contentUri, bitmap)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                val shareText = "मिथिला पंचांग ऐप: \n\n@mithilakshar13"
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(intent, "मिथिला पंचांग ऐप"))
        }

    fun viewToBitmap(view: View): Bitmap {
        val width = view.width
        var height = view.height

        if (view is NestedScrollView) {
            // Measure the total height of the ScrollView content
            height = 0
            for (i in 0 until view.childCount) {
                height += view.getChildAt(i).height
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Fill the canvas with a white background
        canvas.drawColor(android.graphics.Color.WHITE)

        // Draw the view onto the canvas
        view.draw(canvas)
        return bitmap
    }


    }



