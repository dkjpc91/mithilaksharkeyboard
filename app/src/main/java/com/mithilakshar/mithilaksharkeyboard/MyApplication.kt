package com.mithilakshar.mithilaksharkeyboard

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.mithilakshar.mithilaksharkeyboard.utility.AppOpenAdManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus ->
            // Log or handle initialization status if needed
        }

        AppOpenAdManager.loadAd(this)
        registerActivityLifecycleCallbacks(AppOpenAdManager)




    }
}
