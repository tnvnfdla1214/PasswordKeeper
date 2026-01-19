package com.passwordkeeper.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdManager {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    fun loadAd(context: Context) {
        if (isLoading || interstitialAd != null) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            "ca-app-pub-5510790980523633/7485582581",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showAd(activity: Activity, onAdDismissed: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    onAdDismissed()
                    loadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {
                }
            }
            interstitialAd?.show(activity)
        } else {
            onAdDismissed()
            loadAd(activity)
        }
    }
}
