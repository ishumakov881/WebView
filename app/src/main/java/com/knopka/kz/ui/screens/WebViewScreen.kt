package com.knopka.kz.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.walhalla.webview.ChromeView
import com.walhalla.webview.CustomWebViewClient
import com.walhalla.webview.ReceivedError
import com.walhalla.webview.utility.ActivityUtils

@Composable
fun WebViewScreen(url: String) {

    var isLoading by remember { mutableStateOf(false) }
    var switchViews by remember { mutableStateOf(false) }

    var webView by remember { mutableStateOf<WebView?>(null) }

    val context = LocalContext.current




    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AndroidView(
                factory = { context ->
                    SwipeRefreshLayout(context).apply {

                        println("@@@@ $childCount")

                        if (childCount > 0) {
                            var tmp = getChildAt(0)
//                            if(tmp as WebView){
//
//                            }
                        }
//                        if (childCount > 0) {
//                            removeAllViews()
//                        }

                        // Получаем WebView из кэша


                        val onLoadingChange: (Boolean) -> Unit = { loading ->
                            isLoading = loading
                            isRefreshing = false//loading
                        }

                        val client = object : CustomWebViewClient(
                            chromeView = object : ChromeView {

                                private var loadingStartTime = 0L

                                override fun onPageStarted(url: String?) {
                                    loadingStartTime = System.currentTimeMillis()
                                    onLoadingChange(true)
                                }

                                override fun onPageFinished(url: String?) {
                                    onLoadingChange(false)
                                }

                                override fun webClientError(failure: ReceivedError?) {
                                }

                                override fun removeErrorPage() {
                                    switchViews = false
                                }

                                override fun setErrorPage(receivedError: ReceivedError?) {
                                    switchViews = true
                                }

                                override fun openBrowser(url: String) {
                                    ActivityUtils.openBrowser(context, url)
                                }

                            }, context = context
                        ) {

                            private var loadingStartTime = 0L

                            override fun onLoadResource(view: WebView?, url: String?) {
                                super.onLoadResource(view, url)
                                // Хак: если прошло больше 1 секунды - скрываем прогресс
                                if (System.currentTimeMillis() - loadingStartTime > 1000) {
                                    onLoadingChange(false)
                                }
                            }

                        }

                        val cachedWebView = WebViewCache.get(
                                url, context,
                                onLoadingChange = onLoadingChange, client).also { webView = it }

                        // Проверяем, есть ли у WebView родитель
                        (cachedWebView.parent as? android.view.ViewGroup)?.removeView(cachedWebView)

                        // Добавляем WebView в SwipeRefreshLayout
                        addView(cachedWebView)

                        setOnRefreshListener {
                            webView?.reload()
                        }
                    }
                },
                update = { swipeRefresh ->
                    //if (webView?.url != url) {
                    webView?.loadUrl(url)
                    //}
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

object WebViewCache {
    private val cache = mutableMapOf<String, WebView>()

    fun get(
        url: String,
        context: Context,
        onLoadingChange: (Boolean) -> Unit,
        client: CustomWebViewClient
    ): WebView {
        return cache.getOrPut(url) {
            WebView(context).apply {
                configureWebView(this)
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onLoadingChange(true)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onLoadingChange(false)
                    }
                }
                loadUrl(url)
            }
        }.also { webView ->
            webView.webViewClient = client
        }
    }

    fun clear() {
        cache.values.forEach { webView ->
            webView.clearCache(true)
            webView.destroy()
        }
        cache.clear()
    }
} 