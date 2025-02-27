package com.knopka.kz.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebSettings

import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("SetJavaScriptEnabled")
fun configureWebView(webView: WebView) {


    webView.settings.apply {
        // JavaScript и DOM
        javaScriptEnabled = true
        domStorageEnabled = true// Включаем поддержку HTML5 и CSS3
        javaScriptCanOpenWindowsAutomatically = true
        defaultTextEncodingName = "utf-8"
        loadWithOverviewMode = true// Настроить мета-тег для вьюпорта (это также важно для правильного масштабирования)


        // Загрузка и кэширование
        loadsImagesAutomatically = true
        // Зум и масштабирование
        builtInZoomControls = false

        // Геолокация
        setGeolocationEnabled(true)

        // Множественные окна
        //setSupportMultipleWindows(true)

        // Доступ к файлам
        pluginState = WebSettings.PluginState.ON
        allowFileAccess = true
        allowContentAccess = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
        }


        setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        setDomStorageEnabled(true)
        setJavaScriptEnabled(true)
        setLoadsImagesAutomatically(true)
        CookieManager.getInstance().setAcceptCookie(true);
        //setAppCachePath("" + getCacheDir())
        //setAppCacheEnabled(true)
        //setCacheMode(-1)

        // User Agent
        //Mozilla/5.0 (Linux; Android 9; SM-G9880 Build/PQ3B.190801.10101846; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/124.0.6367.82 Mobile Safari/537.36
        //"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0"//


        val originalUA = userAgentString
        userAgentString = fixUserAgent(originalUA).replace("; wv)", ")")
        println("Original UA: $originalUA")
        println("   Fixed UA: ${userAgentString}")



        useWideViewPort = false

        // Включить масштабирование
//        setSupportZoom(true)
//        setBuiltInZoomControls(true)
//        setDisplayZoomControls(false) // Отключить кнопки масштабирования

    }
////
//    val scale = (100 * webView.scale).toInt()
//    webView.setInitialScale(scale)
//
//    webView.getSettings().loadWithOverviewMode = true
//    webView.getSettings().useWideViewPort = true
//    webView.settings.setDefaultZoom(WebSettings.ZoomDensity.FAR)
//
//
//    val webSettings = webView.settings
//
//
//    // Включаем поддержку JavaScript (если нужно)
//    webSettings.javaScriptEnabled = true
//
//
//    // Включаем поддержку viewport meta tag
//    webSettings.useWideViewPort = true
//    webSettings.loadWithOverviewMode = true
//
//
//    // Отключаем масштабирование пользователем
//    webSettings.setSupportZoom(false)
//    webSettings.builtInZoomControls = false
//    webSettings.displayZoomControls = false

    // Получаем настройки WebView
    val webSettings = webView.settings


    // Включаем JavaScript (если потребуется)
    webSettings.javaScriptEnabled = true


    // Устанавливаем поддержку viewport
    webSettings.useWideViewPort = true
    webSettings.loadWithOverviewMode = true






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
    webView.loadUrl("file:///android_asset/viewport.html")

}

fun fixUserAgent(userAgent: String): String {
    // Находим версию Android
    val regex = "Android\\s+([0-9.]+)".toRegex()
    val version = regex.find(userAgent)?.groupValues?.get(1)?.toFloatOrNull() ?: 9.0f

    // Если версия меньше 9, подменяем на 9
    return if (version < 9) {
        val randomVersion = (9..13).random()
        userAgent.replace(regex, "Android $randomVersion")
        //userAgent.replace(regex, "Android 9")
    } else {
        userAgent
    }
}