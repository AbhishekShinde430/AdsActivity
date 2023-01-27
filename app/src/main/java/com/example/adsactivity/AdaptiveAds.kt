package com.example.adsactivity

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.example.adsactivity.databinding.AdsFramelayoutBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError


@Composable
fun AdaptiveAdsBanner(activity: MainActivity,adCode:String) {
    AndroidViewBinding(factory = { layoutInflater, container, bool ->
        val view = AdsFramelayoutBinding.inflate(layoutInflater, container, bool)
        var adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.e(" ads onAdClicked:",  "clicked")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.e(" ads onAdClosed:", "onAdClosed")
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.e(" ads adError:", adError.toString())
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Log.e(" ads:", "onAdImpression")
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e(" ads:", "onAdLoaded")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e("ads:", "onAdOpened")
            }
        }
        loadAdaptiveAds(activity,view.adViewContainer,
            adCode,adListener)
        view
    }){

    }
}