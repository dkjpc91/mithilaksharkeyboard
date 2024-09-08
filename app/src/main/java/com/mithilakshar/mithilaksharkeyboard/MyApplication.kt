package com.mithilakshar.mithilaksharkeyboard

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mithilakshar.mithilaksharkeyboard.Room.UpdatesDatabase
import com.mithilakshar.mithilaksharkeyboard.utility.AppOpenAdManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        analytics = Firebase.analytics
        val database: UpdatesDatabase by lazy {
            UpdatesDatabase.getDatabase(this)
        }

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus ->
            // Log or handle initialization status if needed
        }

        AppOpenAdManager.loadAd(this)
        registerActivityLifecycleCallbacks(AppOpenAdManager)




    }
}
