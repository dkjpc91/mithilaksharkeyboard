package com.mithilakshar.mithilaksharkeyboard

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
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

       binding. anotherImage.setOnClickListener {
            // Handle click for the anotherImage
           val initialHeight = binding.scrollView.height
           val targetHeight = LinearLayout.LayoutParams.MATCH_PARENT
           val animator = ValueAnimator.ofInt(initialHeight, targetHeight)
           animator.addUpdateListener { valueAnimator ->
               val layoutParams = binding.scrollView.layoutParams
               layoutParams.height = valueAnimator.animatedValue as Int
               binding.scrollView.layoutParams = layoutParams
           }
           animator.duration = 500 // Duration in milliseconds
           animator.interpolator = LinearInterpolator()
           animator.start()
        }
    }
}