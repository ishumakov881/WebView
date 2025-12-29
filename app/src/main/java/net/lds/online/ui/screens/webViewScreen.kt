package net.lds.online.ui.screens

import android.app.Activity
import android.webkit.WebView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import net.walhalla.landing.activity.DLog.d
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.walhalla.nointernet.Text
import com.walhalla.webview.BuildConfig
import com.walhalla.webview.ChromeView
import com.walhalla.webview.ReceivedError
import com.walhalla.webview.utility.ActivityUtils

import net.lds.online.ui.WebViewControls


sealed class LoadState() {
    class OnPageStarted(url: String?) : LoadState()
    class OnPageFinished(url: String?) : LoadState()
}

sealed class VWState() {
    object FreshCreated : VWState()
    object FromCache : VWState()
}

@Composable
fun WebViewScreenContent(
    url: String,
    onWebViewReady: (WebView) -> Unit,
    onLoadingChange: (LoadState) -> Unit,
    onWVCreated: (VWState) -> Unit,
    onControlsChanged: () -> Unit,
    onError: (Boolean) -> Unit,

    ) {
    val context = LocalContext.current
    val activity = context as Activity
    var errorMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()/*.windowInsetsPadding(WindowInsets.systemBars).border(3.dp, Color.Red)*/) {

        val isRefreshing = true
        val onRefresh: () -> Unit = {}
        PullToRefreshBox(
            isRefreshing = isRefreshing, // Indicates if the loading indicator should be shown
            onRefresh = onRefresh,     // The action to perform when the user triggers a refresh
            modifier = Modifier
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(22) { index ->
                    ListItem(headlineContent = { Text(text = "@@@") })
                }
            }
        }

        AndroidView(
            modifier = Modifier.border(3.dp, Color.Red).fillMaxSize(),
            factory = { ctx ->
                SwipeRefreshLayout(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    println("@@@@ $childCount")

                    if (childCount > 0) {
                        val tmp = getChildAt(0)
//                            if(tmp as WebView){
//
//                            }
                    }
//                        if (childCount > 0) {
//                            removeAllViews()
//                        }

                    // Получаем WebView из кэша
                    val onPageStarted: (String?) -> Unit = { url ->
                        onLoadingChange(LoadState.OnPageStarted(url))
                        isRefreshing = false
                        errorMessage = "PS: $url"
                    }

                    val onPageFinished: (String?) -> Unit = { url ->
                        onLoadingChange(LoadState.OnPageFinished(url))
                        isRefreshing = false
                        errorMessage = "PF: $url"
                    }

                    val chromeView = object : ChromeView {

                        override fun onPageStarted(url: String?) {
                            onPageStarted(url)
                        }

                        override fun onPageFinished(url: String?) {
                            onPageFinished(url)
                            onControlsChanged()
                        }

                        override fun webClientError(failure: ReceivedError) {
                            errorMessage = failure.toString()
                            onError(true)
                        }

                        override fun removeErrorPage() {
                            onError(false)
                        }

                        override fun setErrorPage(receivedError: ReceivedError) {
                            onError(true)
                        }

                        override fun openBrowser(url: String) {
                            ActivityUtils.openBrowser(context, url)
                        }

                    }

                    if(BuildConfig.DEBUG){
                        WebViewCache.clear()
                    }

                    val cachedWebView = WebViewCache.get(
                        url, activity,
                        //, client
                        chromeView = chromeView,
                        isLoadFromCache = {
                            println("@@@@@@@@@ $it")
                            onWVCreated(if (it) VWState.FromCache else VWState.FreshCreated)
                        }
                    ).also { webView ->
                        onWebViewReady(webView)
                    }
                    cachedWebView.alpha = 0.99f
                    // Проверяем, есть ли у WebView родитель
                    (cachedWebView.parent as? ViewGroup)?.removeView(cachedWebView)

                    // Добавляем WebView в SwipeRefreshLayout
                    addView(cachedWebView)

                    setOnRefreshListener {
                        cachedWebView.reload()
                    }
                }
            },
            update = { swipeRefresh ->
                //if (webView?.url != url) {
                val webView = swipeRefresh.getChildAt(0) as? WebView
                webView?.loadUrl(url)
                //}
            }
        )

        //Text("@ $errorMessage")
    }
}

@Composable
fun FirstLoadIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun LoadingIndicator() = LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))


@Composable
fun WebViewScreen(url: String, onControlsChanged: (WebViewControls) -> Unit) {

    var isLoading by remember { mutableStateOf(false) }

    var switchViews by rememberSaveable { mutableStateOf(false) }

    var isFirstLoad by remember { mutableStateOf(true) }
    var loadingStartTime by remember { mutableLongStateOf(System.currentTimeMillis()) }


    var webView by remember { mutableStateOf<WebView?>(null) }

    val context = LocalContext.current
    val activity = context as Activity


    //val handler = rememberUpdatedState(onControlsChanged)

    val updateControls: () -> Unit = {
        webView?.let {
            onControlsChanged(
                WebViewControls(
                    canGoBack = it.canGoBack(),
                    canGoForward = it.canGoForward(),
                    reload = { it.reload() },
                    goBack = { if (it.canGoBack()) it.goBack() },
                    goForward = { if (it.canGoForward()) it.goForward() }
                )
            )
        }
    }
    DisposableEffect(Unit) {
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//
//            }
//        }
        updateControls()
        onDispose {}
    }


    // Регистрируем launcher для выбора файла
    val fileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (WebViewCache.mUploadMessage != null) {
                var result: Uri? = null
                try {
                    result = if (data == null || data.data == null) {
                        WebViewCache.mCapturedImageURI
                    } else {
                        data.data
                    }
                } catch (e: Exception) {
                    d("@@@$e")
                }
                WebViewCache.mUploadMessage?.onReceiveValue(result)
                WebViewCache.mUploadMessage = null
            }

            if (WebViewCache.mUploadMessages != null) {
                var results: Array<Uri>? = null
                try {
                    if (data != null) {
                        val dataString = data.dataString
                        val clipData = data.clipData
                        if (clipData != null) {
                            results = Array(clipData.itemCount) { i ->
                                clipData.getItemAt(i).uri
                            }
                        } else if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                    if (results == null && WebViewCache.mCapturedImageURI != null) {
                        results = arrayOf(WebViewCache.mCapturedImageURI!!)
                    }
                } catch (e: Exception) {
                    d("@@@$e")
                }
                WebViewCache.mUploadMessages?.onReceiveValue(results ?: arrayOf())
                WebViewCache.mUploadMessages = null
            }
        } else {
            if (WebViewCache.mUploadMessage != null) {
                WebViewCache.mUploadMessage?.onReceiveValue(null)
                WebViewCache.mUploadMessage = null
            }
            if (WebViewCache.mUploadMessages != null) {
                WebViewCache.mUploadMessages?.onReceiveValue(arrayOf())
                WebViewCache.mUploadMessages = null
            }
        }
    }

    // Передаем launcher в WebViewCache
    WebViewCache.fileChooserLauncher = fileChooserLauncher

    Box(modifier = Modifier.fillMaxSize()) {
        WebViewScreenContent(
            url = url,
            onWebViewReady = { webView = it },
            onLoadingChange = {
                when (it) {
                    is LoadState.OnPageFinished -> {
                        isLoading = false
                        isFirstLoad = false
                    }

                    is LoadState.OnPageStarted -> {
                        isLoading = true
                    }
                }

            },
            onControlsChanged = { updateControls() },
            onError = {
                switchViews = it
            },

            onWVCreated = {
                when(it){
                    VWState.FreshCreated -> {
                        isFirstLoad = true
//                        //loadingStartTime = System.currentTimeMillis()
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            isFirstLoad = false
//                            onLoadingChange(false)
//                        }, 2000)
                    }
                    VWState.FromCache -> {
                        isFirstLoad = false
                    }
                }
            }
        )


        if (isLoading) {
            LoadingIndicator()
        }

        // Показываем сообщение об ошибке
        if (switchViews) {
            ErrorContent(modifier = Modifier.align(Alignment.BottomCenter), isLoading = isLoading, onReload = {
                webView?.reload()
                //Toast.makeText(context, "${webView?.url}", Toast.LENGTH_SHORT).show()
            })
        }

        // Показываем анимацию загрузки только при первом запуске, когда WebView еще не создан
        if (isFirstLoad) {
            FirstLoadIndicator()
        }


    }
}

fun onConfirmation__(allowed: Boolean, resources: Array<String>) {
    // Реализация для Composable
}