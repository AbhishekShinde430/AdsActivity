package com.example.adsactivity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

@Composable
fun rememberCustomNativeAdState(
    adUnit: String,
    adListener: AdListener? = null,
    nativeAdOptions: NativeAdOptions? = null,
    activity: Activity
) =  remember(adUnit) {
    NativeAdState(
        activity = activity,
        adUnit = adUnit,
        adListener = adListener,
        adOptions = nativeAdOptions
    )
}

class NativeAdState(
    activity: Activity,
    adUnit: String,
    adListener: AdListener?,
    adOptions: NativeAdOptions?
) {
    val nativeAd = MutableLiveData<NativeAd?>()

    init {
        AdLoader.Builder(activity, adUnit).let {
            if (adOptions != null)
                it.withNativeAdOptions(adOptions)
            else
                it
        }.let {
            if (adListener != null)
                it.withAdListener(adListener)
            else
                it
        }
            .forNativeAd { nativeAd ->
                if (activity.isDestroyed) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                this.nativeAd.postValue(nativeAd)
            }.build().loadAd(AdRequest.Builder().build())
    }
}


