package com.knopka.kz.ui.components

import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                // Получаем настройки WebView
                val webSettings = settings

                // Включаем JavaScript
                webSettings.javaScriptEnabled = true

                // Устанавливаем поддержку viewport
                webSettings.useWideViewPort = true
                webSettings.loadWithOverviewMode = true

                // Включаем поддержку HTML5 и CSS3
                webSettings.domStorageEnabled = true
                webSettings.defaultTextEncodingName = "utf-8"

                // Установка уровня рендеринга для поддержки современных CSS-свойств
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    webSettings.allowFileAccessFromFileURLs = false
                    webSettings.allowUniversalAccessFromFileURLs = false
                }

                // Устанавливаем WebViewClient для перехвата ссылок внутри WebView
                webViewClient = WebViewClient()

                // Загружаем локальную HTML страницу из assets
                loadUrl("file:///android_asset/index.html")
            }
        }
    )
}