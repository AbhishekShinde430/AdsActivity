package com.example.adsactivity

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.*


private var initialLayoutComplete = false
fun loadAdaptiveAds(activity: Activity, ad_view_container: FrameLayout?,
    adcode: String?,
    adListener: AdListener
) {
        if (activity != null && ad_view_container != null  && adcode != null && adcode.isNotEmpty()) {

                try {
                    val adSize: AdSize = getAdSize(activity, ad_view_container)
                    val adView = AdView(activity)
                    ad_view_container.addView(adView)
                    ad_view_container.viewTreeObserver?.addOnGlobalLayoutListener {
                        if (!initialLayoutComplete) {
                            initialLayoutComplete = true
                            adView.adUnitId = adcode
                            adView.setAdSize(adSize)
                            val adRequest = AdRequest.Builder().build()
                            adView.loadAd(adRequest)
                        }
                    }
                    adView.adListener = adListener
                } catch (e: Exception) {
                    e.let { Log.e("Utils Adaptive ads:", e.toString()) }
                }
            }

    }
fun getAdSize(context: Activity, ad_view_container: FrameLayout): AdSize {
    val display = context.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)

    val density = outMetrics.density

    var adWidthPixels = ad_view_container.width.toFloat()
    if (adWidthPixels == 0f) {
        adWidthPixels = outMetrics.widthPixels.toFloat()
    }

    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)

}