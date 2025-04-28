package com.mithilakshar.mithilaksharkeyboard.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mithilakshar.mithilaksharkeyboard.R

class PosterAdapter(
    private val posterItems: List<Map<String, String>>,
    private val onItemClick: (Map<String, String>) -> Unit // Pass the posterItem when clicked
) : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {

    // ViewHolder to hold references to the views
    class PosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutImageView: ImageView = itemView.findViewById(R.id.layoutImageView)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val context = itemView.context // Getting context here for later use
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_layout, parent, false)
        return PosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val posterItem = posterItems[position]

        // Bind data to the views
        holder.categoryName.text = posterItem["hindiname"]

        // Use Glide to load image from URL into the ImageView
        val imageUrl = posterItem["url"]
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(holder.context)
                .load(imageUrl)
                .into(holder.layoutImageView)
        }

        // Set click listener to trigger the onItemClick callback
        holder.itemView.setOnClickListener {
            // Pass the whole posterItem to the onItemClick lambda
            onItemClick(posterItem)
        }
    }

    override fun getItemCount(): Int {
        return posterItems.size
    }
}
