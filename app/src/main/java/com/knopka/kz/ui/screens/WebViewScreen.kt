package com.knopka.kz.ui.screens

import android.webkit.WebView

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

import android.net.Uri
import com.walhalla.landing.activity.DLog.d
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult

@Composable
fun WebViewScreen(url: String) {
    var isLoading by remember { mutableStateOf(false) }
    var switchViews by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    val context = LocalContext.current

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

fun onConfirmation__(allowed: Boolean, resources: Array<String>) {
    // Реализация для Composable
}



