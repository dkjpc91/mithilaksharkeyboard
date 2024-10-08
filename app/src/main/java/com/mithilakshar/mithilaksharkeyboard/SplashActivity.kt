package com.mithilakshar.mithilaksharkeyboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.util.Log
import android.view.animation.AnimationUtils
import com.google.android.gms.ads.MobileAds
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivitySplashBinding
import com.mithilakshar.mithilaksharkeyboard.utility.AppOpenAdManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein)

        binding.apply {
            splash.startAnimation(fadeInAnimation)
            splashTxt.startAnimation(fadeInAnimation)

        }


        Handler(Looper.getMainLooper()).postDelayed({

            //showAdOrProceed()
            startMainActivity()
          // Optional: finish the current activity if you don't want to keep it in the back stack
        }, 3000)



    }

    private fun showAdOrProceed() {
        AppOpenAdManager.showAdIfAvailable(this) {
            // Proceed to the main activity after the ad is dismissed
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Close the splash activity
    }

}