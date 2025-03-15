package com.mithilakshar.mithilaksharkeyboard.UI

import CustomModifier
import android.content.res.ColorStateList

import android.graphics.Typeface

import android.graphics.drawable.Drawable

import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater

import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge


import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityDisplayLayoutBinding
import com.mithilakshar.mithilaksharkeyboard.utility.GestureTouchListener



class DisplayLayoutActivity : AppCompatActivity() {

    lateinit var binding: ActivityDisplayLayoutBinding
    private lateinit var layoutContainer: FrameLayout
    var isCustomFontApplied = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayLayoutBinding.inflate(layoutInflater)
        enableEdgeToEdge() // Enabling edge-to-edge UI
        setContentView(binding.root)

        // Initialize RelativeLayout instead of LinearLayout
        layoutContainer = binding.root

        // Ensure secure window flag is set
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        // Extract layout data from the intent
        val layoutName = intent.getStringExtra("layout") ?: ""
        val txt1 = intent.getStringExtra("txt1") ?: "Default Title"
        val txt2 = intent.getStringExtra("txt2") ?: "Default Message"
        val image1Url = intent.getStringExtra("image1")
        val image2Url = intent.getStringExtra("image2")
        val bgUrl = intent.getStringExtra("bg")






        // Convert the layout name into a resource ID
        val layoutId = resources.getIdentifier(layoutName, "layout", packageName)
        if (layoutId == 0) {
            Log.e("DisplayLayoutActivity", "Error: Layout $layoutName not found")
            return
        }



        val inflater = LayoutInflater.from(this)
        val layoutView = inflater.inflate(layoutId, null) // Inflate the layout

// Get the root view of the activity
        val rootView = binding.root

// Set layout parameters to make sure it fills the screen
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

// Add the inflated layout as the background view
        layoutView.layoutParams = layoutParams
        rootView.addView(layoutView, 0)


        // Dynamically find views inside the layout
        val txt1View = layoutView.findViewById<TextView>(R.id.txt1)
        val txt2View = layoutView.findViewById<TextView>(R.id.txt2)
        val image1View = layoutView.findViewById<ImageView>(R.id.image1)
        val image2View = layoutView.findViewById<ImageView>(R.id.image2)
        val bgView = layoutView.findViewById<LinearLayout>(R.id.bg)
        txt2View?.text = txt2
        txt1View?.text = txt1

        // Load images using Glide
        image1Url?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.m)
                .error(R.drawable.logo)
                .into(image1View)
        } ?: run {
            Log.e("DisplayLayoutActivity", "Error: image1Url is null")
        }

        image2Url?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.m)
                .error(R.drawable.logo)
                .into(image2View)
        } ?: run {
            Log.e("DisplayLayoutActivity", "Error: image2Url is null")
        }

        // Load background image correctly using Glide
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
        } ?: run {
            Log.e("DisplayLayoutActivity", "Error: bgUrl is null")
        }

        // Apply Gesture Listener to individual views
        val gestureTouchListener = GestureTouchListener(this, layoutView)


        binding.fab.setOnClickListener {
            Toast.makeText(this, "toast fab ", Toast.LENGTH_SHORT).show()

            val customModifier = CustomModifier(this)

            // Show the custom dialog
            customModifier.showDialog(txt1View, txt2View, image1View, image2View,bgView,txt1, txt2, image1Url, image2Url, bgUrl)
        }

        binding.mt.setOnClickListener {
            // Toggle Font
            if (isCustomFontApplied) {
                // Revert to the default font (system font)
                txt1View.typeface = Typeface.DEFAULT
                txt2View.typeface = Typeface.DEFAULT

                // Change FAB color to yellow
                binding.fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow))
            } else {
                // Load the custom font from the assets folder
                val typeface = ResourcesCompat.getFont(this, R.font.mithilakshar_dkj)

                // Set the custom font to TextView1 and TextView2
                txt1View.typeface = typeface
                txt2View.typeface = typeface

                // Change FAB color to green
                binding.fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
            }

            // Toggle the flag
            isCustomFontApplied = !isCustomFontApplied
        }




        txt1View?.setOnTouchListener(gestureTouchListener)
        txt2View?.setOnTouchListener(gestureTouchListener)
        image1View?.setOnTouchListener(gestureTouchListener)
        image2View?.setOnTouchListener(gestureTouchListener)


    }







}
