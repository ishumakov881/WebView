package com.knopka.kz.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.walhalla.webview.ChromeView
import com.walhalla.webview.CustomWebViewClient
import com.walhalla.webview.ReceivedError
import com.walhalla.webview.utility.ActivityUtils
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement

@Composable
fun WebViewScreen(url: String) {

    var isLoading by remember { mutableStateOf(false) }

    var switchViews by remember { mutableStateOf(false) }

    var webView by remember { mutableStateOf<WebView?>(null) }

    val context = LocalContext.current




    Box(modifier = Modifier.fillMaxSize()) {
        Column {


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
                            isRefreshing = false
                        }

                        val cachedWebView = WebViewCache.get(
                            url, context,
                            onLoadingChange = onLoadingChange//, client
                        ).also { webView = it }

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

        // Показываем анимацию загрузки только при первом запуске, когда WebView еще не создан
//        if (isFirstLoad) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = androidx.compose.ui.Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(48.dp),
//                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
//                )
//            }
//        }
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
        }

        // Показываем сообщение об ошибке
//        if (switchViews) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Text(
//                        text = "Нет подключения к интернету",
//                        style = MaterialTheme.typography.titleLarge,
//                        textAlign = TextAlign.Center
//                    )
//
//                    Text(
//                        text = "Проверьте подключение и попробуйте снова",
//                        style = MaterialTheme.typography.bodyMedium,
//                        textAlign = TextAlign.Center,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                    )
//
//                    Button(
//                        onClick = {
//                            context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
//                        }
//                    ) {
//                        Text("Открыть настройки сети")
//                    }
//
//                    OutlinedButton(
//                        onClick = {
//                            webView?.reload()
//                        }
//                    ) {
//                        Text("Повторить")
//                    }
//                }
//            }
//        }
    }
}

object WebViewCache {
    private val cache = mutableMapOf<String, WebView>()

    val loadingStartTime = System.currentTimeMillis()
    var isFirstLoad = false

    fun get(
        url: String,
        context: Context,
        onLoadingChange: (Boolean) -> Unit
        //client: CustomWebViewClient
    ): WebView {
        return cache.getOrPut(url) {

//            val loadingStartTime by remember{ mutableLongStateOf(System.currentTimeMillis()) }
//            var isFirstLoad by remember { mutableStateOf(true) }


            val client = object : CustomWebViewClient(
                chromeView = object : ChromeView {

                    override fun onPageStarted(url: String?) {
                        //loadingStartTime = System.currentTimeMillis()
                        onLoadingChange(true)
                    }

                    override fun onPageFinished(url: String?) {
                        onLoadingChange(false)
                    }

                    override fun webClientError(failure: ReceivedError?) {
                    }

                    override fun removeErrorPage() {
                        //switchViews = false
                    }

                    override fun setErrorPage(receivedError: ReceivedError?) {
                        //switchViews = true
                    }

                    override fun openBrowser(url: String) {
                        ActivityUtils.openBrowser(context, url)
                    }

                }, context = context
            ) {


                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    // Хак: если прошло больше 1 секунды - скрываем прогресс
                    if (System.currentTimeMillis() - loadingStartTime > 1_000) {
                        if (isFirstLoad) {
                            isFirstLoad = false
                        }
                        onLoadingChange(false)
                    }
                }
            }

            WebView(context).apply {
                configureWebView(this)
                webViewClient = client
                webChromeClient=...б
                loadUrl(url)
            }
        }.also { webView ->
//            val loadingStartTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
//            var isFirstLoad by remember { mutableStateOf(true) }

            val client = object : CustomWebViewClient(
                chromeView = object : ChromeView {

                    override fun onPageStarted(url: String?) {
                        //loadingStartTime = System.currentTimeMillis()
                        onLoadingChange(true)
                    }

                    override fun onPageFinished(url: String?) {
                        onLoadingChange(false)
                    }

                    override fun webClientError(failure: ReceivedError?) {
                    }

                    override fun removeErrorPage() {
                        //switchViews = false
                    }

                    override fun setErrorPage(receivedError: ReceivedError?) {
                        //switchViews = true
                    }

                    override fun openBrowser(url: String) {
                        ActivityUtils.openBrowser(context, url)
                    }

                }, context = context
            ) {


                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    // Хак: если прошло больше 1 секунды - скрываем прогресс
                    if (System.currentTimeMillis() - loadingStartTime > 1_000) {
                        if (isFirstLoad) {
                            isFirstLoad = false
                        }
                        onLoadingChange(false)
                    }
                }

            }
            webView.webViewClient = client
            webChromeClient=...
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



