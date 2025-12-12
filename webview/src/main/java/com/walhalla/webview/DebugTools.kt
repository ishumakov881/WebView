package com.walhalla.webview

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri

object DebugTools {
    private val TAG: String = DebugTools::class.java.simpleName

    fun printParams(s: String, url: String) {
        if (BuildConfig.DEBUG) {
            val uri = url.toUri()
            val domain = uri.host
            Log.d(TAG, "$s $url $domain")
        }
    }
}
