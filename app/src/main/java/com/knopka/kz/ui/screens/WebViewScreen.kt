package com.knopka.kz.ui.screens

import android.app.Activity
import android.webkit.WebView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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

import com.walhalla.webview.ChromeView
import com.walhalla.webview.ReceivedError

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.walhalla.landing.activity.DLog.d
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.knopka.kz.BuildConfig.VERSION_NAME
import com.knopka.kz.R

import com.walhalla.webview.ActivityUtils

@Composable
fun WebViewScreen(url: String) {



    var isFullscreen by remember { mutableStateOf(false) }
    var fullscreenView: View? by remember { mutableStateOf(null) }


    var isLoading by remember { mutableStateOf(false) }

    var switchViews by remember { mutableStateOf(false) }

    var isFirstLoad by remember { mutableStateOf(true) }
    var loadingStartTime by remember { mutableLongStateOf(System.currentTimeMillis()) }


    var webView by remember { mutableStateOf<WebView?>(null) }

    val context = LocalContext.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher


    BackHandler(enabled = !isFullscreen) {
        if (webView?.canGoBack() == true) {
            webView?.goBack() // Возвращаемся в WebView
        } else {
            backDispatcher?.onBackPressed() // Стандартное поведение (выход из приложения или переход назад)
        }
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

//    Box(modifier = Modifier.fillMaxSize()) {
//        Column {
//
//
//            AndroidView(
//                factory = { context ->
//                    SwipeRefreshLayout(context).apply {
//
//                        println("@@@@ $childCount")
//
//                        if (childCount > 0) {
//                            var tmp = getChildAt(0)
////                            if(tmp as WebView){
////
////                            }
//                        }
////                        if (childCount > 0) {
////                            removeAllViews()
////                        }
//
//                        // Получаем WebView из кэша
//
//
//                        val onLoadingChange: (Boolean) -> Unit = { loading ->
//                            isLoading = loading
//                            isRefreshing = false
//                        }
//
//                        val chromView = object : ChromeView {
//
//                            override fun onPageStarted(url: String?) {
//                                onLoadingChange(true)
//                            }
//
//                            override fun onPageFinished(url: String?) {
//                                onLoadingChange(false)
//                            }
//
//                            override fun webClientError(failure: ReceivedError) {
//                            }
//
//                            override fun removeErrorPage() {
//                                switchViews = (false)
//                            }
//
//                            override fun setErrorPage(receivedError: ReceivedError) {
//                                switchViews = (true)
//                            }
//
//                            override fun openBrowser(url: String) {
//                                ActivityUtils.openBrowser(context, url)
//                            }
//
//                        }
//                        val cachedWebView = WebViewCache.get(
//                            url, context,
//                            //, client
//                            chromView = chromView,
//                            isFirstLoad = {
//                                println("@@@$it")
//                                isFirstLoad = it
//                                if (isFirstLoad) {
//                                    loadingStartTime = System.currentTimeMillis()
//                                    Handler(Looper.getMainLooper()).postDelayed({
//                                        if (isFirstLoad) {
//                                            isFirstLoad = false
//                                            onLoadingChange(false)
//                                        }
//                                    }, 2000)
//                                }
//                            },
//                            onEnterFullscreen = { view ->
//                                isFullscreen = true
//                                fullscreenView = view
//
//
//                                Toast.makeText(context, "#@@@@", Toast.LENGTH_SHORT).show()
//                            },
//                            onExitFullscreen = {
//                                isFullscreen = false
//                                fullscreenView = null
//                            }
//                        ).also { webView = it }
//                        cachedWebView.alpha = 0.99f
//                        // Проверяем, есть ли у WebView родитель
//                        (cachedWebView.parent as? android.view.ViewGroup)?.removeView(cachedWebView)
//
//                        // Добавляем WebView в SwipeRefreshLayout
//                        addView(cachedWebView)
//
//                        setOnRefreshListener {
//                            webView?.reload()
//                        }
//                    }
//                },
//                update = { swipeRefresh ->
//                    //if (webView?.url != url) {
//                    webView?.loadUrl(url)
//                    //}
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//
//        // Показываем анимацию загрузки только при первом запуске, когда WebView еще не создан
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
//        if (isLoading) {
//            LinearProgressIndicator(
//                modifier = Modifier.fillMaxWidth(),
//                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
//            )
//        }
//
//        // Показываем сообщение об ошибке
////        if (switchViews) {
////            Box(
////                modifier = Modifier
////                    .fillMaxSize()
////                    .padding(16.dp),
////                contentAlignment = Alignment.Center
////            ) {
////                Column(
////                    horizontalAlignment = Alignment.CenterHorizontally,
////                    verticalArrangement = Arrangement.spacedBy(16.dp)
////                ) {
////                    Text(
////                        text = "Нет подключения к интернету",
////                        style = MaterialTheme.typography.titleLarge,
////                        textAlign = TextAlign.Center
////                    )
////
////                    Text(
////                        text = "Проверьте подключение и попробуйте снова",
////                        style = MaterialTheme.typography.bodyMedium,
////                        textAlign = TextAlign.Center,
////                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
////                    )
////
////                    Button(
////                        onClick = {
////                            context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
////                        }
////                    ) {
////                        Text("Открыть настройки сети")
////                    }
////
////                    OutlinedButton(
////                        onClick = {
////                            webView?.reload()
////                        }
////                    ) {
////                        Text("Повторить")
////                    }
////                }
////            }
////        }
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isFullscreen) {
            Column {
                AndroidView(
                    factory = { context ->
                        SwipeRefreshLayout(context).apply {
                            val onLoadingChange: (Boolean) -> Unit = { loading ->
                                isLoading = loading
                                isRefreshing = false
                            }

                            val chromView = object : ChromeView {
                                override fun onPageStarted(url: String?) {
                                    onLoadingChange(true)
                                }

                                override fun onPageFinished(url: String?) {
                                    onLoadingChange(false)
                                }

                                override fun webClientError(failure: ReceivedError) {}

                                override fun removeErrorPage() {
                                    switchViews = false
                                }

                                override fun setErrorPage(receivedError: ReceivedError) {
                                    switchViews = true
                                }

                                override fun openBrowser(url: String) {
                                    ActivityUtils.openBrowser(context, url)
                                }
                            }

                            val cachedWebView = WebViewCache.get(
                                url,
                                context,
                                chromView = chromView,
                                isFirstLoad = {
                                    isFirstLoad = it
                                    if (isFirstLoad) {
                                        loadingStartTime = System.currentTimeMillis()
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            if (isFirstLoad) {
                                                isFirstLoad = false
                                                onLoadingChange(false)
                                            }
                                        }, 2000)
                                    }
                                },
                                onEnterFullscreen = { view ->
                                    isFullscreen = true
                                    fullscreenView = view
                                },
                                onExitFullscreen = {
                                    isFullscreen = false
                                    fullscreenView = null
                                }
                            ).also { webView = it }

                            cachedWebView.alpha = 0.99f

                            (cachedWebView.parent as? android.view.ViewGroup)?.removeView(cachedWebView)
                            addView(cachedWebView)

                            setOnRefreshListener {
                                println("--y--")
                                webView?.reload()
                            }
                        }
                    },
                    update = { swipeRefresh ->
                        println("--x--")
                        //Not use :: webView?.loadUrl(url)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // Полноэкранный режим
            if (fullscreenView != null) {
                AndroidView(
                    factory = { context ->
                        FrameLayout(context).apply {
                            addView(fullscreenView)
                        }
                    },
                    update = { frameLayout ->
                        if (fullscreenView != null) {
                            frameLayout.removeAllViews()
                            frameLayout.addView(fullscreenView)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }



        // Показываем анимацию загрузки только при первом запуске
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


        if (isFirstLoad) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface) // Фон сплеш-экрана
                    .padding(16.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Иконка приложения (круглая)
                    Image(
                        painter = painterResource(id = R.drawable.app_icon), // Замените на вашу иконку
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape) // Делаем иконку круглой
                            .background(Color.LightGray.copy(alpha = 0.3f)) // Опционально: добавляем фон
                    )

                    // 2. Название программы
                    Text(
                        text = stringResource(id = R.string.app_name), // Название программы из strings.xml
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    // 3. Версия приложения
                    Text(
                        text = "Version ${VERSION_NAME}", // Версия из BuildConfig
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Показываем индикатор загрузки
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
        }
    }
}

fun onConfirmation__(allowed: Boolean, resources: Array<String>) {
    // Реализация для Composable
}



