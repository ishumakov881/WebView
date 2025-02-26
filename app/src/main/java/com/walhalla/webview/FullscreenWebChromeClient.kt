package com.walhalla.webview

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient

open abstract class MyWebChromeClient(value: Callback) : WebChromeClient() {

    interface Callback {
        //fun a123(chromeView: ChromeView, mView: UWView)
        fun onProgressChanged(progress: Int)

        fun openFileChooser(uploadMsg: ValueCallback<Uri?>?, s: String?)

        fun openFileChooser(
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        )
    }






}
