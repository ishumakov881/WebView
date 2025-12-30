package com.walhalla.webview


object WebViewAppConfig {
    // true for enabling Google Analytics, should be true in production release
    const val ANALYTICS: Boolean = true

    // app id and client key for Parse push notifications,
    // keep these constants empty if you do not want to use push notifications,
    // if you want to use push notifications, setup these constants and
    // uncomment necessary permissions, service and receivers in AndroidManifest.xml file
    const val PARSE_APPLICATION_ID: String = ""
    const val PARSE_CLIENT_KEY: String = ""

    // true for opening webview links in external web browser rather than directly in the webview
    const val OPEN_LINKS_IN_EXTERNAL_BROWSER: Boolean = false


    // rules for opening links in internal webview,
    // if URL link contains the string, it will be loaded in internal webview,
    // these rules have higher priority than OPEN_LINKS_IN_EXTERNAL_BROWSER option
    val LINKS_OPENED_IN_INTERNAL_WEBVIEW: Array<String> = arrayOf(
        "target=webview",
        "target=internal"
    )
}
