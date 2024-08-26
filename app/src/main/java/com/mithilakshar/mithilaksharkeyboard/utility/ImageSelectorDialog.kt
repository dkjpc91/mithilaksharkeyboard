package com.mithilakshar.mithilaksharkeyboard.utility

// ImageSelectorDialog.kt
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.adapter.ImageAdapter

class ImageSelectorDialog(context: Context, private val onImageSelected: (Int) -> Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_selector)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewImages)

        // Example image resource IDs, replace with your own
        val imageResources = listOf(
            R.drawable.page,
            R.drawable.page1,
            R.drawable.up
        )

        val adapter = ImageAdapter(imageResources) { selectedImage ->
            onImageSelected(selectedImage)
            dismiss()
        }

        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter = adapter
    }
}
