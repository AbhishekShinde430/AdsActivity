package com.example.adsactivity

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*

@Composable
fun LoadBannerAd(adId: String, size: AdSize) {

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(size)
                adUnitId = adId
                loadAd(AdRequest.Builder().build())

                adListener = object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("TAG", "onAdFailedToLoad: $adError")
                    }
                    override fun onAdImpression() {

                    }
                }
            }
        }
    )
}