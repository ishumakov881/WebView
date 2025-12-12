package com.walhalla.webview

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout


/*
*подскажи почему в WebView в плеере при нажатии фулскрин экран поворачивается он │
│     весь белый но видео нет, как работает onShowCustomView
* */

open class MyWebChromeClient(
    private val activity: Activity,
    private val webView: WebView,
    val callback: Callback) : WebChromeClient() {

    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var originalOrientation: Int = activity.requestedOrientation


    // Добавляем метод для проверки полноэкранного режима
    fun isInFullscreen() = customView != null

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {

        if (customView != null) {
            callback?.onCustomViewHidden()
            return
        }

        // Фиксируем текущее состояние
        originalOrientation = activity.requestedOrientation
        //originalSystemUiVisibility = activity.window.decorView.systemUiVisibility

        // Сохраняем View и Callback
        customView = view
        customViewCallback = callback

        // Настраиваем кастомное View
        view?.let {
            it.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )

            // Критически важные настройки
            it.keepScreenOn = true
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }

        // Добавляем View в корневой layout
        val decor = activity.window.decorView as FrameLayout
        decor.addView(view)

        // Скрываем WebView и системные элементы

        webView.visibility = View.GONE
        //webView.setBackgroundColor(Color.RED)

        //setFullscreen(true)

        // Фиксируем ориентацию
        //activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

//        if (customView != null) {
//            customView?.setBackgroundColor(Color.BLACK)
//            callback?.onCustomViewHidden()
//            return
//        }
//
//        // Сохраняем View и Callback
//        customView = view
//        customViewCallback = callback
//
//        // Добавляем View в корневой layout
//        val decorView = activity.window.decorView as FrameLayout
//        decorView.addView(customView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
//
//        // Скрываем WebView
//        webView.visibility = View.GONE
//
//        // Устанавливаем полноэкранный режим
//        activity.window.decorView.systemUiVisibility = (
//                View.SYSTEM_UI_FLAG_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                )
//
//        // Фиксируем ориентацию экрана
//        //activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onHideCustomView() {
        if (customView == null) return

        // Убираем View из корневого layout
        val decorView = activity.window.decorView as FrameLayout
        decorView.removeView(customView)

        // Восстанавливаем WebView
        webView.visibility = View.VISIBLE

        // Вызываем callback
        customViewCallback?.onCustomViewHidden()
        customView = null
        customViewCallback = null

        // Восстанавливаем ориентацию экрана
        activity.requestedOrientation = originalOrientation

        // Восстанавливаем системные UI элементы
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }


    //    private TextView titleText;
    //
    //
    //    public MyWebChromeClient(TextView titleText) {
    //
    //
    //        this.titleText = titleText;
    //    }
    override fun onProgressChanged(webView: WebView, i: Int) {
        super.onProgressChanged(webView, i)
        callback.onProgressChanged(i)
    }

    interface Callback {
        fun onProgressChanged(progress: Int)

        fun openFileChooser(uploadMsg: ValueCallback<Uri>, s: String?)

        fun openFileChooser(
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams?
        )
    }

    override fun onGeolocationPermissionsShowPrompt(
        origin: String,
        callback: GeolocationPermissions.Callback
    ) {
        callback.invoke(origin, true, false)
    }

    // openFileChooser for Android 3.0+
    // openFileChooser for Android < 3.0
    @JvmOverloads
    fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String? = "") {
//        mUploadMessage = uploadMsg;
//        callback.openImageChooser();

        callback.openFileChooser(uploadMsg, acceptType)
    }

    // For Lollipop 5.0+ Devices
    override fun onShowFileChooser(mWebView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams): Boolean {
//        mUploadMessages = filePathCallback;
//        callback.openImageChooser();

        callback.openFileChooser(filePathCallback, fileChooserParams)
        return true
    }

    //openFileChooser for other Android versions
    fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?, capture: String?) {
        openFileChooser(uploadMsg, acceptType)
    }
}