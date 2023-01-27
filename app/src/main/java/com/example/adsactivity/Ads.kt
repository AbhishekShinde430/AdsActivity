package com.example.adsactivity

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

object Ads {

    fun initialize(context: Context){
        MobileAds.initialize(context) {}
    }

    fun loadSticyAds(context: Context,view:FrameLayout){
        val adView = AdView(context)
        view.addView(adView)
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        adView.setAdSize(AdSize.BANNER)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}