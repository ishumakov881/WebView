package com.walhalla.webview

import android.app.Activity
import android.content.Context

interface ChromeView {
    fun onPageStarted(url: String?)

    fun onPageFinished( /*WebView view, */url: String?)

    fun webClientError(failure: ReceivedError)

    fun removeErrorPage()

    fun setErrorPage(receivedError: ReceivedError)

    fun openBrowser(url: String)

    //void openOauth2(Activity context, String url);
    //void mAcceptPressed(String url);
    //void eventRequest(BodyClass bodyClass);
}