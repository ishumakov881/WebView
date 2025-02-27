package com.walhalla.testtest

import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем WebView
        val webView = WebView(this)
        setContentView(webView)


        // Получаем настройки WebView
        val webSettings = webView.settings


        // Включаем JavaScript (если потребуется)
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



        // Для загрузки внешних URL можно использовать следующий метод:
        // webView.loadUrl("https://example.com");

        // Устанавливаем WebViewClient для перехвата ссылок внутри WebView
        webView.webViewClient = WebViewClient()

        // Загружаем локальную HTML страницу из assets
        webView.loadUrl("file:///android_asset/index.html")
    }
}