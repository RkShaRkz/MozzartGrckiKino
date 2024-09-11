package org.example.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

class FragmentWebView(private val navController: NavController? = null)  : Fragment() {

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString("url", "https://mozzartbet.com/sr/lotto-animation/26#")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use ComposeView to wrap the compose ability to call compose's setContent from a plain fragment
        return ComposeView(requireContext()).apply {
            setContent {
                WebViewScreen(url)
            }
        }
    }

    @Composable
    fun WebViewScreen(url: String) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "WebView") })
            },
            content = {
                WebViewContainer(url)
            }
        )
    }

    @Composable
    fun WebViewContainer(url: String) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient() // Ensures that the WebView opens the page inside the app instead of using a browser
                    loadUrl(url)
                }
            },
            update = { webView ->
                webView.loadUrl(url) // Update URL if necessary
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String, navController: NavController? = null): FragmentWebView {
            val fragment = FragmentWebView(navController)
            val args = Bundle().apply {
                putString("url", url)
            }
            fragment.arguments = args
            return fragment
        }
    }
}

