package com.knopka.kz.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView

@SuppressLint("SetJavaScriptEnabled")
fun configureWebView(webView: WebView) {
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