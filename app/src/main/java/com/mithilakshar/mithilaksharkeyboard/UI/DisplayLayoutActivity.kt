package com.mithilakshar.mithilaksharkeyboard.UI

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mithilakshar.mithilaksharkeyboard.R

class DisplayLayoutActivity : AppCompatActivity() {

    private lateinit var layoutContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_layout)

        layoutContainer = findViewById(R.id.layoutContainer)

        // Extract all layout data from Intent
        val layoutName = intent.getStringExtra("layout") ?: ""
        val txt1 = intent.getStringExtra("txt1") ?: "Default Title"
        val txt2 = intent.getStringExtra("txt2") ?: "Default Message"
        val image1Url = intent.getStringExtra("image1")
        val image2Url = intent.getStringExtra("image2")
        val bgUrl = intent.getStringExtra("bg")

        Log.d("DisplayLayoutActivity", "Received Layout: $layoutName | txt1: $txt1, txt2: $txt2, img1: $image1Url, img2: $image2Url, bg: $bgUrl")

        if (layoutName.isNullOrEmpty()) {
            Log.e("DisplayLayoutActivity", "Error: No layout name provided!")
            return
        }

        // Convert layout name to resource ID
        val layoutId = resources.getIdentifier(layoutName, "layout", packageName)

        if (layoutId == 0) {
            Log.e("DisplayLayoutActivity", "Error: Layout $layoutName not found")
            return
        }

        // Inflate and display only the selected layout
        val inflater = LayoutInflater.from(this)
        val layoutView = inflater.inflate(layoutId, layoutContainer, false)
        layoutContainer.addView(layoutView)

        // Find views dynamically
        val txt1View = layoutView.findViewById<TextView>(R.id.txt1)
        val txt2View = layoutView.findViewById<TextView>(R.id.txt2)
        val image1View = layoutView.findViewById<ImageView>(R.id.image1)
        val image2View = layoutView.findViewById<ImageView>(R.id.image2)
        val bgView = layoutView.findViewById<LinearLayout>(R.id.bg)

        // Set text
        txt1View?.text = txt1
        txt2View?.text = txt2

        // Load images using Glide
        image1Url?.let { Glide.with(this).load(it).placeholder(R.drawable.m).error(R.drawable.logo).into(image1View!!) }
        image2Url?.let { Glide.with(this).load(it).placeholder(R.drawable.m).error(R.drawable.logo).into(image2View!!) }

        // ✅ Load background correctly
        bgUrl?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.logo)
                .error(R.drawable.m)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        bgView?.background = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        bgView?.background = placeholder
                    }
                })
        }
    }
}
