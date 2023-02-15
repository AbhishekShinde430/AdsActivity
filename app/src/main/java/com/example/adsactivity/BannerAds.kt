package com.example.adsactivity

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun LoadBannerAd(adId: String, size: AdSize) {
    Log.d("BANNER", "function: called")

    val view = rememberAdview(adId = adId, adSize = size)

    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "Advert Here",
        )
    } else {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                Log.d("BANNER", "view: created")
                view

            },
            update = {ad->
                Log.d("BANNER", "ad: updated")
                view.loadAd(AdRequest.Builder().build())
            }
        )

        DisposableEffect(key1 = true) {
            onDispose {
                view.destroy()

            }
        }
    }


}

@Composable
fun rememberAdview(
    adId: String,
    adSize: AdSize
): AdView {
    val context = LocalContext.current
    val adView = remember{
        val adView = AdView(context)
        adView.setAdSize(adSize)
        adView.adUnitId =adId
        adView
    }
    return adView
}


