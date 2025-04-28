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

        val txt1Text = "सादर आमंत्रण प्रियजन, हम अहाँकेँ सादर आमंत्रण दैत छी कि अहाँ अपन परिवार संग आबि [आयोजनक नाम] मे भाग लिअऽ। अहाँक उपस्थिति सँ एहिठामक आनंद आ उत्साह आरो बढ़त।"
        val txt2Text = "तारीख: [तारीख जोड़ू]"
        val txt3Text = " स्थान: [स्थान जोड़ू]"
        val txt4Text = "समय: [समय जोड़ू]"
        val txt5Text = "अहाँक आगमन केर प्रतीक्षा मे। सादर, [अहाँक नाम] "



        val image1Url = "https://i.pinimg.com/736x/c0/51/31/c05131dfb06af2251012298652b99359.jpg"
        val image2Url = "https://i.pinimg.com/736x/22/d9/ff/22d9ffa8c2561f0f5199579a01d43545.jpg"
        val bgUrl = "https://i.pinimg.com/736x/21/d7/4c/21d74caf242f50e4355bfdfae6aae6c2.jpg"

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