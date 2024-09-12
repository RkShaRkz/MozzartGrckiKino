package org.example.project

import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    // Since this is a bridge between typical Android views and Compose, it allows
                    // us to pull necessary android-ish garbage workarounds to fix the UI so...
                    // Create LayoutParams and set them on the WebView, and center it properly
                    val params = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    layoutParams = params
                    foregroundGravity = Gravity.CENTER

                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            // I dont think this modifier does anything or works good but lets leave it there
            // I think the root cause was that 'foregroundGravity' defaults to START|TOP
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize() // Doesn't work good...
        )
    }
}

