package com.knopka.kz.ui.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler

import android.os.Looper
import android.os.Message
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.lds.webview.BuildConfig
import com.lds.wvrss.ui.CustomWebViewClient
import com.walhalla.landing.activity.BaseWPresenter
import com.walhalla.landing.activity.DLog

import com.walhalla.webview.ChromeView

import com.walhalla.webview.FullscreenWebChromeClient
import com.walhalla.webview.UWView
import java.io.File
import java.io.IOException

object WebViewCache {


    var mUploadMessage: ValueCallback<Uri?>? = null
    var mUploadMessages: ValueCallback<Array<Uri>>? = null
    var mCapturedImageURI: Uri? = null

    private val cache = mutableMapOf<String, WebView>()

    //val loadingStartTime = System.currentTimeMillis()

    var fileChooserLauncher: ActivityResultLauncher<Intent>? = null

    fun get(
        url: String,
        context: Context,

        isFirstLoad: (Boolean) -> Unit,
        chromView: ChromeView,


        onEnterFullscreen: (View) -> Unit,
        onExitFullscreen: () -> Unit

    ): WebView {



        val SESSION_NAME = "113"


        println("@@@${cache.size}")
        val isFromCache = cache.containsKey(url)
        println("@@@CACHE: URL exists in cache: $isFromCache")
        if (!isFromCache) {  // Новый WebView
            println("@@@CACHE: Using FRESH WebView")
            isFirstLoad(true)
        } else {  // Из кеша
            println("@@@CACHE: Using CACHED WebView")
            isFirstLoad(false)
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        val chromeClient = object : FullscreenWebChromeClient(object : Callback {


            override fun onProgressChanged(progress: Int) {
                println("@@@@$progress")
            }

            override fun openFileChooser(uploadMsg: ValueCallback<Uri?>?, s: String?) {
                mUploadMessage = uploadMsg
                DLog.d("@mUploadMessage@$mUploadMessage")
                openImageChooser()
            }


            override fun openFileChooser(
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ) {
                mUploadMessages = filePathCallback
                DLog.d("@mUploadMessages@$mUploadMessages")
                openImageChooser()
            }

            override fun onEnterFullscreen(view: View) {
                onEnterFullscreen(view)
            }

            override fun onExitFullscreen() {
                onExitFullscreen()
            }

            private fun openImageChooser() {
                try {
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(context, "@@@", Toast.LENGTH_SHORT).show()
                    }
                    var photoFile: File? = null
                    try {
                        photoFile = BaseWPresenter.create_image(context)
                    } catch (ex: IOException) {
                        Log.e("@@@", "photoFile file creation failed", ex)
                    }
                    mCapturedImageURI = Uri.fromFile(photoFile)

                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                    val i = Intent(Intent.ACTION_GET_CONTENT)
                    i.addCategory(Intent.CATEGORY_OPENABLE)
                    i.type = "image/*"
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

                    val chooserIntent = Intent.createChooser(i, "Image Chooser")
                    chooserIntent.putExtra(
                        Intent.EXTRA_INITIAL_INTENTS,
                        arrayOf<Parcelable>(takePictureIntent)
                    )

                    // Запускаем через launcher
                    fileChooserLauncher?.launch(chooserIntent)
                } catch (e: Exception) {
                    DLog.handleException(e)
                }
            }
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        }) {


            override fun onPermissionRequest(request: PermissionRequest) {
                Handler(Looper.getMainLooper()).postDelayed({
                    request.grant(request.resources)
                }, 1000)
            }

            override fun onPermissionRequestCanceled(request: PermissionRequest) {
                // Обработка отмены запроса разрешений
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(
                view: WebView,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message
            ): Boolean {
                val newWebView = WebView(view.context)
                newWebView.settings.javaScriptEnabled = true

                val builder = AlertDialog.Builder(view.context)
                builder.setView(newWebView)
                builder.setPositiveButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }

                val dialog = builder.create()
                newWebView.webChromeClient = this
                newWebView.webViewClient = object : WebViewClient() {}
                dialog.show()

                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()

                return true
            }
        }

        return cache.getOrPut(url) {


            UWView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Ширина
                    ViewGroup.LayoutParams.MATCH_PARENT  // Высота
                )
                val wv = this

                configureWebView(wv)
                val client = object : CustomWebViewClient(
                    sessionName = SESSION_NAME,
                    mView = wv,  // передаем текущий WebView
                    chromeView = chromView,
                    context = context
                ) {


                    override fun onLoadResource(view: WebView?, url: String?) {
                        super.onLoadResource(view, url)
                        // Хак: если прошло больше 1 секунды - скрываем прогресс
//                        if (System.currentTimeMillis() - loadingStartTime > 2_000) {
//                            if (isFirstLoad) {
//                                isFirstLoad = false
//                            }
//                            onLoadingChange(false)
//                        }
                    }

                }

                webViewClient = client
                webChromeClient = chromeClient

                if(!isFromCache){
                    loadUrl(url)
                }
            }
        }.also { webView ->


            val client = object : CustomWebViewClient(
                sessionName = SESSION_NAME,
                webView,
                chromeView = chromView, context = context
            ) {


                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
//                    // Хак: если прошло больше 1 секунды - скрываем прогресс
//                    if (System.currentTimeMillis() - loadingStartTime > 1_000) {
//                        if (isFirstLoad) {
//                            isFirstLoad = false
//                        }
//                        onLoadingChange(false)
//                    }
                }

            }
            webView.webViewClient = client
            webView.webChromeClient = chromeClient  // Используем тот же объект
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