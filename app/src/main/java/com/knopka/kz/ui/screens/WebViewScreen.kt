package com.knopka.kz.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebSettings
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@SuppressLint("SetJavaScriptEnabled")
private fun configureWebView(webView: WebView) {
    webView.settings.apply {
        // Основные настройки
        setSupportZoom(false)
        defaultTextEncodingName = "utf-8"
        loadWithOverviewMode = true

        // Загрузка и кэширование
        loadsImagesAutomatically = true

        // JavaScript и DOM
        javaScriptEnabled = true
        domStorageEnabled = true
        javaScriptCanOpenWindowsAutomatically = true

        // Зум и масштабирование
        builtInZoomControls = false

        // Геолокация
        setGeolocationEnabled(true)

        // Множественные окна
        setSupportMultipleWindows(true)

        // Доступ к файлам
        pluginState = WebSettings.PluginState.ON
        allowFileAccess = true
        allowContentAccess = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
        }

        // User Agent
        //Mozilla/5.0 (Linux; Android 9; SM-G9880 Build/PQ3B.190801.10101846; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/124.0.6367.82 Mobile Safari/537.36
        //"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0"//


        val originalUA = userAgentString
        userAgentString = fixUserAgent(originalUA).replace("; wv)", ")")
        println("Original UA: $originalUA")
        println("   Fixed UA: ${userAgentString}")

    }
}


private fun fixUserAgent(userAgent: String): String {
    // Находим версию Android
    val regex = "Android\\s+([0-9.]+)".toRegex()
    val version = regex.find(userAgent)?.groupValues?.get(1)?.toFloatOrNull() ?: 9.0f

    // Если версия меньше 9, подменяем на 9
    return if (version < 9) {
        userAgent.replace(regex, "Android 9")
    } else {
        userAgent
    }
}

@Composable
fun WebViewScreen(url: String) {
    var isLoading by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }

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
                        val cachedWebView = WebViewCache.get(url, context, onLoadingChange = { loading ->
                            isLoading = loading
                            isRefreshing = false//loading
                        }).also { webView = it }

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
        context: android.content.Context,
        onLoadingChange: (Boolean) -> Unit
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
            webView.webViewClient = object : WebViewClient() {

                private var loadingStartTime = 0L

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    // Хак: если прошло больше 1 секунды - скрываем прогресс
                    if (System.currentTimeMillis() - loadingStartTime > 1000) {
                        onLoadingChange(false)
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    loadingStartTime = System.currentTimeMillis()
                    onLoadingChange(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onLoadingChange(false)
                }
            }
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