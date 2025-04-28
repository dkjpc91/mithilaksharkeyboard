package com.mithilakshar.mithilaksharkeyboard.adapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mithilakshar.mithilaksharkeyboard.R

class LayoutAdapter(
    private val categoryMap: List<Map<String, Any?>>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<LayoutAdapter.ViewHolder>() {

    private val TAG = "LayoutAdapter"
    private val PAGE_SIZE = 5
    private var visibleCategoryMap: MutableList<Map<String, Any?>> = mutableListOf()
    var isLoading = false

    init {
        loadMoreItems()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = visibleCategoryMap.getOrNull(position) ?: return
        val context = holder.itemView.context
        val name = itemData["name"] as? String ?: "Unknown"
        val txt1 = itemData["txt1"] as? String ?: "Default Title"
        val txt2 = itemData["txt2"] as? String ?: "Default Message"
        val txt3 = itemData["txt3"] as? String ?: "Default Message"
        val txt4 = itemData["txt4"] as? String ?: "Default Message"
        val txt5 = itemData["txt5"] as? String ?: "Default Message"
        val image1Url = itemData["image1"] as? String
        val image2Url = itemData["image2"] as? String
        val bgUrl = itemData["bg"] as? String
        val layoutName = itemData["layout"] as? String ?: return
        val layoutId = context.resources.getIdentifier(layoutName, "layout", context.packageName)

        holder.categoryName.text = name
        if (layoutId == 0) {
            holder.layoutImageView.setImageResource(R.drawable.mithilakshar)
            return
        }

        val layoutView = LayoutInflater.from(context).inflate(layoutId, null, false)
        val rootLayout = layoutView.findViewById<View>(R.id.bg)
        val txt1View = layoutView.findViewById<TextView>(R.id.txt1)
        val txt2View = layoutView.findViewById<TextView>(R.id.txt2)
        val txt3View = layoutView.findViewById<TextView>(R.id.txt3)
        val txt4View = layoutView.findViewById<TextView>(R.id.txt4)
        val txt5View = layoutView.findViewById<TextView>(R.id.txt5)

        val image1View = layoutView.findViewById<ImageView>(R.id.image1)
        val image2View = layoutView.findViewById<ImageView>(R.id.image2)

        txt1View?.text = txt1
        txt2View?.text = txt2
        txt3View?.text = txt3
        txt4View?.text = txt4
        txt5View?.text = txt5


        var imagesLoaded = 0
        val totalImages = listOfNotNull(image1Url, image2Url, bgUrl).size

        fun checkAndSetBitmap() {
            if (++imagesLoaded >= totalImages) {
                holder.layoutImageView.setImageBitmap(getBitmapFromView(layoutView))
            }
        }

        loadImage(image1Url, context, image1View, ::checkAndSetBitmap)
        loadImage(image2Url, context, image2View, ::checkAndSetBitmap)
        loadBackgroundImage(bgUrl, context, rootLayout, ::checkAndSetBitmap)

        holder.cardView.setOnClickListener {
            Log.d(TAG, "Card clicked for name: $name")
            onItemClick(name)
        }
    }

    override fun getItemCount(): Int = visibleCategoryMap.size

    fun loadMoreItems() {
        if (isLoading) return
        val start = visibleCategoryMap.size
        val end = minOf(start + PAGE_SIZE, categoryMap.size)
        if (start < end) {
            isLoading = true
            visibleCategoryMap.addAll(categoryMap.subList(start, end))
            notifyDataSetChanged()
            isLoading = false
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutImageView: ImageView = itemView.findViewById(R.id.layoutImageView)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

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

    private fun loadImage(url: String?, context: android.content.Context, imageView: ImageView?, onLoadComplete: () -> Unit) {
        url?.let {
            Glide.with(context)
                .asBitmap()
                .load(it)
                .placeholder(R.drawable.m)
                .error(R.drawable.logo)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        imageView?.setImageBitmap(resource)
                        onLoadComplete()
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } ?: onLoadComplete()
    }

    private fun loadBackgroundImage(url: String?, context: android.content.Context, view: View?, onLoadComplete: () -> Unit) {
        url?.let {
            Glide.with(context)
                .asBitmap()
                .load(it)
                .placeholder(R.drawable.logo)
                .error(R.drawable.m)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        view?.background = BitmapDrawable(context.resources, resource)
                        onLoadComplete()
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } ?: onLoadComplete()
    }
}

fun RecyclerView.addPaginationListener(adapter: LayoutAdapter) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (!adapter.isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                adapter.loadMoreItems()
            }
        }
    })
}