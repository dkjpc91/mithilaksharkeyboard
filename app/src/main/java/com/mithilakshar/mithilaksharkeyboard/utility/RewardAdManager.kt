package com.mithilakshar.mithilaksharkeyboard.utility


import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener

import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardAdManager(private val context: Context) {
    private var rewardedAd: RewardedAd? = null
    private var adUnitId: String = "ca-app-pub-3940256099942544/5224354917"  // Replace with your actual ad unit ID

    fun initialize() {
        // Initialize the Mobile Ads SDK
        MobileAds.initialize(context) {}
    }

    fun loadRewardedAd(onAdLoaded: () -> Unit, onAdFailedToLoad: (String) -> Unit) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
                onAdFailedToLoad(adError.message)
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                onAdLoaded()
            }
        })
    }

    fun showRewardedAd(activity: Activity, onUserEarnedReward: () -> Unit, onAdNotLoaded: () -> Unit) {
        if (rewardedAd != null) {
            rewardedAd?.show(activity, OnUserEarnedRewardListener {
                onUserEarnedReward()
            })
        } else {
            onAdNotLoaded()
        }
    }
}
