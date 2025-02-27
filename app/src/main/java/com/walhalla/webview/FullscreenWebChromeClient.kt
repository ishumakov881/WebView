package com.walhalla.webview

import android.net.Uri
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient

open abstract class FullscreenWebChromeClient(var callback: Callback) : WebChromeClient() {

    interface Callback {
        //fun a123(chromeView: ChromeView, mView: UWView)
        fun onProgressChanged(progress: Int)

        fun openFileChooser(uploadMsg: ValueCallback<Uri?>?, s: String?)

        fun openFileChooser(
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        )


        fun onEnterFullscreen(view: View)
        fun onExitFullscreen()
    }


    private var customView: View? = null

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (customView != null) {
            callback?.onCustomViewHidden()
            return
        }

        customView = view
        this.callback.onEnterFullscreen(view!!)
    }

    override fun onHideCustomView() {
        if (customView == null) return

        customView = null
        callback.onExitFullscreen()
    }


}
