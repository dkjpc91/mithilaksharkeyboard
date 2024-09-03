package com.mithilakshar.mithilaksharkeyboard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    var isExpanded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.nextImage.setOnClickListener {
            // Handle click for the nextImage
            // For example, start a new Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            animateScrollView()
        }, 1000)

        // Initialize a flag to track the expanded state



    }


    fun animateScrollView() {
        val initialHeight = binding.scrollView.height
        val collapseHeight = 0 // Collapse to zero height
        val expandHeight = if (isExpanded) 200 else 1650 // Collapse to 200, expand to 1650

        // First animation: Collapse to zero height
        val collapseAnimator = ValueAnimator.ofInt(initialHeight, collapseHeight)
        collapseAnimator.addUpdateListener { valueAnimator ->
            val layoutParams = binding.scrollView.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            binding.scrollView.layoutParams = layoutParams
        }
        collapseAnimator.duration = 300 // Duration for collapse in milliseconds
        collapseAnimator.interpolator = LinearInterpolator()

        // Second animation: Expand to target height
        val expandAnimator = ValueAnimator.ofInt(collapseHeight, expandHeight)
        expandAnimator.addUpdateListener { valueAnimator ->
            val layoutParams = binding.scrollView.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            binding.scrollView.layoutParams = layoutParams
        }
        expandAnimator.duration = 700 // Duration for expand in milliseconds
        expandAnimator.interpolator = LinearInterpolator()

        // Start the collapse animation, then the expand animation
        collapseAnimator.start()
        collapseAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Start the expand animation after collapse completes
                expandAnimator.start()
            }
        })

        // Toggle the expanded state
        isExpanded = !isExpanded
    }






}