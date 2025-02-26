package com.walhalla.webview

interface ChromeView {
    fun onPageStarted(url: String?)
    fun onPageFinished(url: String?)



    fun webClientError(failure: ReceivedError)
    fun removeErrorPage()
    fun setErrorPage(receivedError: ReceivedError)

    fun openBrowser(url: String)

}
