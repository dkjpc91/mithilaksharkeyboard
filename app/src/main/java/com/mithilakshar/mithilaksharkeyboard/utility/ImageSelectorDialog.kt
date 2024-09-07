package com.mithilakshar.mithilaksharkeyboard.utility

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.adapter.ImageAdapter
import com.mithilakshar.mithilaksharkeyboard.adapter.ImagedburlAdapter

class ImageSelectorDialog(
    context: Context,
    private val imageResources: List<Int>? = null, // List of drawable resource IDs (optional)
    private val imageMap: List<String>? = null, // Map of image paths/URIs (optional)
    private val onImageSelected: (Any) -> Unit // Image resource ID or URI string will be passed
) : Dialog(context) {

    companion object {
        // Factory method for drawable resources
        fun newInstanceForResources(
            context: Context,
            imageResources: List<Int>,
            onImageSelected: (Any) -> Unit
        ): ImageSelectorDialog {
            return ImageSelectorDialog(
                context = context,
                imageResources = imageResources,
                onImageSelected = onImageSelected
            )
        }

        // Factory method for image paths/URIs
        fun newInstanceForImageMap(
            context: Context,
            imageMap:List<String>? ,
            onImageSelected: (Any) -> Unit
        ): ImageSelectorDialog {
            return ImageSelectorDialog(
                context = context,
                imageMap = imageMap,
                onImageSelected = onImageSelected
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_selector)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewImages)

        when {
            // Case 1: imageResources is provided
            imageResources != null -> {
                val adapter = ImageAdapter(imageResources) { selectedImage ->
                    onImageSelected(selectedImage)
                    dismiss()
                }
                recyclerView.layoutManager = GridLayoutManager(context, 2)
                recyclerView.adapter = adapter
            }

            // Case 2: imageMap is provided
            imageMap != null -> {
                val adapter = ImagedburlAdapter(imageMap) { selectedImageUri ->
                    onImageSelected(selectedImageUri)
                    dismiss()
                }
                recyclerView.layoutManager = GridLayoutManager(context, 2)
                recyclerView.adapter = adapter
            }

            // No valid input
            else -> throw IllegalArgumentException("Either imageResources or imageMap must be provided")
        }
    }
}
