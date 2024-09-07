package com.mithilakshar.mithilaksharkeyboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Using Glide for image loading
import com.mithilakshar.mithilaksharkeyboard.R

class ImagedburlAdapter(
    private val images: List<String>,  // List of image URLs or file paths
    private val onItemClick: (String) -> Unit // Callback with image URL or file path
) : RecyclerView.Adapter<ImagedburlAdapter.ImageViewHolder>() { // Updated class name

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                onItemClick(images[adapterPosition]) // Pass the image file path/URI on click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Load image using Glide
        val imagePath = images[position]
        Glide.with(holder.itemView.context)
            .load(imagePath) // Load the image from the file path/URI
            .placeholder(R.drawable.feather) // Optional placeholder while loading
            .into(holder.imageView)
    }

    override fun getItemCount() = images.size
}
