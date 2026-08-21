package com.pratistha.reminderapp.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun BannerAdView(adUnitId: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isAdLoaded by remember { mutableStateOf(false) }
    
    val adSize = rememberAdSize(context)

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isAdLoaded) Modifier else Modifier.height(0.dp)),
        factory = { ctx ->
            AdView(ctx).apply {
                this.adUnitId = adUnitId
                setAdSize(adSize)
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        isAdLoaded = true
                    }
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        isAdLoaded = false
                    }
                }
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> adView.resume()
                    Lifecycle.Event.ON_PAUSE -> adView.pause()
                    Lifecycle.Event.ON_DESTROY -> adView.destroy()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
        }
    )
}

@Composable
private fun rememberAdSize(context: android.content.Context): AdSize {
    val displayMetrics = context.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    val density = displayMetrics.density
    val adWidth = (widthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}
