package com.mithilakshar.mithilaksharkeyboard.adapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mithilakshar.mithilaksharkeyboard.R

class LayoutListAdapter(
    private val categoryMap: List<Map<String, Any?>>,
    private val onItemClick: (Map<String, Any?>) -> Unit  // onItemClick now accepts a Map
) : RecyclerView.Adapter<LayoutListAdapter.ViewHolder>() {

    private val TAG = "LayoutListAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = categoryMap.getOrNull(position) ?: return

        val context = holder.itemView.context
        val name = itemData["name"] as? String ?: "Unknown"
        val txt1 = itemData["txt1"] as? String ?: "Default Title"
        val txt2 = itemData["txt2"] as? String ?: "Default Message"
        val image1Url = itemData["image1"] as? String
        val image2Url = itemData["image2"] as? String
        val bgUrl = itemData["bg"] as? String
        val layoutName = itemData["layout"] as? String ?: return
        val layoutId = context.resources.getIdentifier(layoutName, "layout", context.packageName)

        Log.d(TAG, "Binding category: $name -> txt1: $txt1, txt2: $txt2, image1: $image1Url, image2: $image2Url, bg: $bgUrl")

        holder.categoryName.text = name

        if (layoutId == 0) {
            holder.layoutImageView.setImageResource(R.drawable.mithilakshar)
            return
        }

        val layoutView = LayoutInflater.from(context).inflate(layoutId, null, false)
        val rootLayout = layoutView.findViewById<View>(R.id.bg)
        val txt1View = layoutView.findViewById<TextView>(R.id.txt1)
        val txt2View = layoutView.findViewById<TextView>(R.id.txt2)
        val image1View = layoutView.findViewById<ImageView>(R.id.image1)
        val image2View = layoutView.findViewById<ImageView>(R.id.image2)

        txt1View?.text = txt1
        txt2View?.text = txt2

        // Load images and set them before capturing bitmap
        var imagesLoaded = 0
        val totalImages = listOfNotNull(image1Url, image2Url, bgUrl).size

        fun checkAndSetBitmap() {
            if (++imagesLoaded >= totalImages) {
                holder.layoutImageView.setImageBitmap(getBitmapFromView(layoutView))
            }
        }

        image1Url?.let {
            Glide.with(context)
                .asBitmap()
                .load(it)
                .placeholder(R.drawable.m)
                .error(R.drawable.logo)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        image1View?.setImageBitmap(resource)
                        checkAndSetBitmap()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } ?: checkAndSetBitmap()

        image2Url?.let {
            Glide.with(context)
                .asBitmap()
                .load(it)
                .placeholder(R.drawable.m)
                .error(R.drawable.logo)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        image2View?.setImageBitmap(resource)
                        checkAndSetBitmap()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } ?: checkAndSetBitmap()

        bgUrl?.let {
            Glide.with(context)
                .asBitmap()
                .load(it)
                .placeholder(R.drawable.logo)
                .error(R.drawable.m)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        rootLayout?.background = android.graphics.drawable.BitmapDrawable(context.resources, resource)
                        checkAndSetBitmap()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } ?: checkAndSetBitmap()

        holder.cardView.setOnClickListener {
            Log.d(TAG, "Card clicked for name: $name")
            onItemClick(itemData)  // Pass the entire item data on click
        }
    }

    override fun getItemCount(): Int = categoryMap.size

    // ViewHolder class to hold item views
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutImageView: ImageView = itemView.findViewById(R.id.layoutImageView)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    // Function to capture the layout view as a Bitmap
    private fun getBitmapFromView(view: View): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        return Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888).apply {
            Canvas(this).apply { view.draw(this) }
        }
    }
}
