package com.example.adsactivity

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun NativeAdViewCompose(
    modifier: Modifier = Modifier,
    content: @Composable (nativeAdView: NativeAdView) -> Unit
) {
    AndroidView(modifier = modifier, factory = {
        NativeAdView(it)
    }, update = {
        val composeView = ComposeView(it.context)
        it.removeAllViews()
        it.addView(composeView)
        composeView.setContent {
            content(it)
        }
    })
}

@Composable
fun NativeAdImage(
    modifier: Modifier = Modifier,
    drawable: Drawable?,
    contentDescription: String,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,

    ) = Image(
    painter = rememberDrawablePainter(drawable = drawable),
    contentDescription = contentDescription,
    contentScale = contentScale,
    modifier = modifier,
    colorFilter = colorFilter,
    alpha = alpha,
    alignment = alignment,
)

@Composable
fun NativeAdMediaView(modifier: Modifier = Modifier, setup: (MediaView) -> Unit) =
    AndroidView(modifier = modifier, factory = { MediaView(it) }, update = {
        setup(it)
    })

@Composable
fun NativeAdMediaView(
    modifier: Modifier = Modifier,
    nativeAdView: NativeAdView,
    mediaContent: MediaContent,
    scaleType: ImageView.ScaleType
) = AndroidView(modifier = modifier, factory = { MediaView(it) }, update = {
    nativeAdView.mediaView = it
    nativeAdView.mediaView?.setMediaContent(mediaContent)
    nativeAdView.mediaView?.setImageScaleType(scaleType)
})



@Composable
fun NativeAdView(
    modifier: Modifier = Modifier, getView: (ComposeView) -> Unit, content: @Composable () -> Unit
) {
    AndroidView(modifier = modifier, factory = { ComposeView(it) }, update = {
        it.setContent {
            content()
        }
        getView(it)
    })

}

@Composable
fun NativeAdsSection(
    nativeAdState: AdState,
    rememberNativeAdState: NativeAdState?
) {
    rememberNativeAdState?.let {
        val nativeAd by it.nativeAd.observeAsState()
        // Text("Native Ad", style = TextStyle(fontWeight = FontWeight.Bold))
        NativeAdsDesign(nativeAd)
        when {
            nativeAdState.isSuccess -> Text("Native ad loaded successfully")
            nativeAdState.isError -> Text("Native Ad load failed: ${nativeAdState.errorMessage}")
        }
    }
}



@Composable
private fun NativeAdsDesign(nativeAd: NativeAd?) {
    if (nativeAd != null)
        NativeAdViewCompose { nativeAdView ->
            nativeAdView.setNativeAd(nativeAd)
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    //Icon
                    NativeAdView(getView = {
                        nativeAdView.iconView = it
                    }, modifier = Modifier.weight(1f)) {
                        NativeAdImage(
                            drawable = nativeAd.icon?.drawable,
                            contentDescription = "Icon",
                            modifier = Modifier.wrapContentSize()
                        )
                    }

                    //Headline
                    NativeAdView(getView = {
                        nativeAdView.headlineView = it
                    },modifier = Modifier.weight(3f)) {
                        Text(
                            text = nativeAd.headline ?: "-",
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 20.sp
                        )
                    }


                }





                //Body
                NativeAdView(getView = {
                    nativeAdView.bodyView = it
                },) {
                    Text(text = nativeAd.body ?: "-", fontSize = 15.sp)
                }

                //video
                nativeAd.mediaContent?.let { mediaContent ->
                    NativeAdMediaView(
                        modifier = Modifier.fillMaxWidth(),
                        nativeAdView = nativeAdView,
                        mediaContent = mediaContent,
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    )

                }
            }
        }
}




