package com.mithilakshar.mithilaksharkeyboard.UI

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityLayoutTesterBinding
import android.graphics.drawable.Drawable
import android.view.View

class LayoutTester : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityLayoutTesterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewBinding
        binding = ActivityLayoutTesterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Data
        val layoutName = "दुर्गा पूजा"

        val txt1Text = "दुर्गा पूजा के शुभकामना!"
        val txt2Text = "मिथिला मे दुर्गा पूजा भक्ति, संस्कृति आ सामाजिक एकता केर प्रतीक अछि। शक्तिक उपासना संग-संग एहि अवसर पर लोकक हर्षोल्लास अद्भुत होइत अछि।"
        val txt3Text = "माँ दुर्गा अहाँक जीवन मे शक्ति, शांति आ समृद्धि प्रदान करथि। दुर्गा पूजा केर मंगलमय शुभकामना!"
        val txt4Text = ""
        val txt5Text = "नाम : दीपक कुमार झा"



        val image1Url = "https://i.pinimg.com/736x/89/50/ee/8950ee2165fa9dda202ea759586a4c40.jpg"
        val image2Url = "https://i.pinimg.com/736x/b9/b1/55/b9b155e6b25629664e2e0cef5c59fd3e.jpg"
        val bgUrl = "https://i.pinimg.com/736x/8f/96/71/8f9671098540133de4cab1eb272606d3.jpg"

        // Set text to TextViews
        binding.txt1.text = txt1Text
        binding.txt2.text = txt2Text
        binding.txt3.text = txt3Text
        binding.txt4.text = txt4Text
        binding.txt5.text = txt5Text

        binding.txt4.visibility = View.GONE
        // Load images using Glide
        Glide.with(this)
            .load(image1Url)
            .placeholder(R.drawable.m) // Add a placeholder drawable
            .error(R.drawable.smile) // Add an error drawable
            .into(binding.image1)

        Glide.with(this)
            .load(image2Url)
            .placeholder(R.drawable.m) // Add a placeholder drawable
            .error(R.drawable.mbg) // Add an error drawable
            .into(binding.image2)

        // Load background image dynamically into the parent LinearLayout
        Glide.with(this)
            .load(bgUrl)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.bg.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle cleanup if needed
                }
            })
    }
}