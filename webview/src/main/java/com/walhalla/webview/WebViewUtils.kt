package com.walhalla.webview

import android.content.ActivityNotFoundException
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient.ERROR_CONNECT
import android.webkit.WebViewClient.ERROR_HOST_LOOKUP
import android.webkit.WebViewClient.ERROR_PROXY_AUTHENTICATION
import android.webkit.WebViewClient.ERROR_TIMEOUT
import android.widget.Toast
import androidx.core.net.MailTo
import androidx.core.net.ParseException
import androidx.core.net.toUri
import com.walhalla.webview.CustomWebViewClient.Companion.TAG
import com.walhalla.webview.CustomWebViewClient.Companion.isConnected
import com.walhalla.webview.CustomWebViewClient.Companion.offlineMessageHtml
import com.walhalla.webview.utility.ActivityUtils
import com.walhalla.webview.utility.DownloadUtility
import java.util.Locale

fun isDownloadableFile(downloadFileTypes: Array<String>, url: String): Boolean {
    var url = url
    val index = url.indexOf("?")
    if (index > -1) {
        url = url.substring(0, index)
    }
    url = url.lowercase(Locale.getDefault())
    for (type in downloadFileTypes) {
        if (url.endsWith(type)) return true
    }
    return false
}
private fun isSameDomain(url: String, baseDomain: String?): Boolean {
    val uri = url.toUri()
    val domain = uri.host
    val result =
        if (domain != null && (domain.endsWith(".$baseDomain") || domain == baseDomain)) {
            true
        } else {
            false
        }
    println("isSameDomain: $domain $baseDomain $result")
    //o.php?
    return result
}

fun CustomWebViewClient.handleErrorCode(webView: WebView, failure: ReceivedError) {

    val theErrorisalreadyshown = uiState is WebUiState.Error
    //@@@@val errorOnTheSamePage = isErrorOnMainPage(failure.failingUrl)
    val errorCode = failure.errorCode
    //@@@@println("errorOnTheSamePage $errorOnTheSamePage, ERROR_CODE: $errorCode $theErrorisalreadyshown")
    //ERR_PROXY_CONNECTION_FAILED, we use Charles
    if (theErrorisalreadyshown) return
    //@@@@if (!errorOnTheSamePage) return

    when (errorCode) {
        (ERROR_PROXY_AUTHENTICATION) -> {
            setErrorPage(failure)
        }

        (ERROR_HOST_LOOKUP /*ERR_INTERNET_DISCONNECTED*/) -> { //-2 ERR_NAME_NOT_RESOLVED
            //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
            //@@@ webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
            setErrorPage(failure)
            //Toast.makeText(context, "@@@", Toast.LENGTH_SHORT).show();
            webClientError(failure)
        }

        (ERROR_TIMEOUT) -> { //-8 ERR_CONNECTION_TIMED_OUT @@ -8 aka ERR_CONNECTION_RESET
            //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
            //@@@ webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
            setErrorPage(failure)
            webClientError(failure)
        }


        (ERROR_CONNECT) -> { // -6	net::ERR_CONNECTION_REFUSED
            //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
            //@@@ webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
            setErrorPage(failure)
            webClientError(failure)
        }

        (-14) -> { // -14 is error for file not found, like 404.
            //Skip
        }

        else -> {
            setErrorPage(failure)
            webClientError(failure)
        }
        //ERR_CONNECTION_REFUSED

    }
}
fun CustomWebViewClient.handleUrl(baseDomain: String?, view: WebView, url: String): Boolean {
    val var0 = isDownloadableFile(downloadFileTypes, url)
    if (var0) {
        Toast.makeText(context, R.string.fragment_main_downloading, Toast.LENGTH_LONG).show()
        DownloadUtility.downloadFile(context, url, DownloadUtility.getFileName(url))
        return true
    } else if (url.startsWith("https://telegram.me") || url.startsWith("tg:") || url.startsWith(
            "https://t.me/"
        )
    ) {
        ActivityUtils.starttg(context, url)
        return true //handle itself
    } else if (url.startsWith("file:///android_asset")) {
        Toast.makeText(context, "@@@@@@@@@", Toast.LENGTH_SHORT).show()
        return false
    } else if ((url.startsWith("http://") || url.startsWith("https://"))) {
//            if (url.startsWith("https://accounts.google.com/o/oauth2") || url.contains("redirect_uri=")) {
//                // Новое значение для параметра redirect_uri
//                String encodedRedirectUri = "";
//                try {
//                    String newRedirectUri = context.getPackageName() + "://app/";
//                    encodedRedirectUri = URLEncoder.encode(newRedirectUri, "UTF-8");
//                } catch (UnsupportedEncodingException ignored) {
//                }
//                // Разбиваем строку по символу '&' для получения параметров
//                String[] parts = url.split("&");
//
//                // Проходим по каждому параметру и заменяем redirect_uri на новое значение
//                StringBuilder modifiedUrlBuilder = new StringBuilder();
//                for (String part : parts) {
//                    if (part.startsWith("redirect_uri=")) {
//                        // Заменяем старое значение на новое
//                        modifiedUrlBuilder.append("redirect_uri=").append(encodedRedirectUri);
//                    } else {
//                        // Сохраняем остальные параметры без изменений
//                        modifiedUrlBuilder.append(part);
//                    }
//                    // Добавляем '&' после каждого параметра, кроме последнего
//                    modifiedUrlBuilder.append("&");
//                }
//
//                // Удаляем лишний '&' в конце строки
//                String modifiedUrl = modifiedUrlBuilder.toString().substring(0, modifiedUrlBuilder.length() - 1);
//                println(TAG + "@" + modifiedUrl);
//                chromeView.openOauth2(context, modifiedUrl);
//                return true;
//            }


        //println(TAG + "@c@");
        // determine for opening the link externally or internally


        var openInExternalApp = isLinkExternal(url) //openInExternalApp app
        val internal = DownloadUtility.isLinkInternal(url) //internal webView
        if (!openInExternalApp && !internal) {
            openInExternalApp = WebViewAppConfig.OPEN_LINKS_IN_EXTERNAL_BROWSER
        }
        //My new Code
        if (url.endsWith(".apk")) {
            chromeView?.openBrowser(url)
            return true
        }

        // open the link
        if (openInExternalApp) {
            println(TAG + "@@@")
            chromeView?.openBrowser(url)
            return true
        } else {
            if (isCheckSameDomainEnabled) {
                if (isSameDomain(url, baseDomain)) {
                    println(
                        TAG + "NOT_OVERRIDE:isSameDomain: $baseDomain :: $url"
                    )
                    return false
                } else {
                    println(
                        TAG + "blocked: $url, $baseDomain"
                    )

                    //var 1
                    //url blocked
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show()
                    }
                    return true
                }
            } else {
                //@@@ showActionBarProgress(true);
                println(TAG + "NOT_OVERRIDE: ... $url")
                return false
            }
        }
    } else if (url.startsWith("mailto:")) {
        try {
            val mailTo = MailTo.parse(url)
            ActivityUtils.startEmailActivity(
                context, mailTo.to ?: "", mailTo.subject, mailTo.body
            )
        } catch (ignored: ParseException) {
        }
        return true
    } else if (url.startsWith("whatsapp://send?phone=")) {
        val url2 =
            "https://api.whatsapp.com/send?phone=" + url.replace("whatsapp://send?phone=", "")
        val intent = Intent(Intent.ACTION_VIEW, url2.toUri())
        intent.addFlags(
            Intent.FLAG_ACTIVITY_FORWARD_RESULT
                    or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                    or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
        )
            .setPackage("com.whatsapp")
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=com.whatsapp".toUri()
                )
            )
        }
        return true
    } else if (url.startsWith("https://api.whatsapp.com/send?phone=") || url.startsWith("https://api.whatsapp.com/send/?phone=")) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(
            Intent.FLAG_ACTIVITY_FORWARD_RESULT
                    or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                    or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
        )
            .setPackage("com.whatsapp")
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=com.whatsapp".toUri()
                )
            )
        }
        return true //bs
    } else if (url.startsWith("whatsapp://send?text=")) {
        val uri = url.toUri()
        val msg = uri.getQueryParameter("text")
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
        sendIntent.setType("text/plain")
        sendIntent.setPackage("com.whatsapp")
        try {
            context.startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=com.whatsapp".toUri()
                )
            )
        }
        return true //bs
    } else if (url.startsWith("viber:")) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=com.viber.voip".toUri()
                )
            )
        }
        return true //bs
    } else if (url.startsWith("tel:")) {
        ActivityUtils.startCallActivity(context, url)
        return true
    } else if (url.startsWith("sms:")) {
        ActivityUtils.startSmsActivity(context, url)
        return true
    } else if (url.startsWith("geo:")) {
        ActivityUtils.startMapSearchActivity(context, url)
        return true
    } else if (url.startsWith("yandexnavi:")) {
        ActivityUtils.startyandexnavi(context, url)
        return true
    } else if (url.startsWith("intent://")) {
        if (url.startsWith("intent://maps.yandex")) {
            ActivityUtils.startMapYandex(context, url.replace("intent://", "https://"))
            return true
        }
        //bnk            else if (InAppBrowserUtils.isNspb(url)) {
//bnk                return InAppBrowserUtils.handleNspb(view, url);
//bnk            } else if (url.startsWith("intent://pay.mironline.ru")) {
//bnk                InAppBrowserUtils.paymironlineru(context, url);
//bnk                return true;
//bnk            }
        return false
    } else {
        if (isConnected) {
            // return false to let the WebView handle the URL
            return false
        } else {
            // show the proper "not connected" message
            view.loadData(offlineMessageHtml, "text/html", "utf-8")
            // return true if the host application wants to leave the current
            // WebView and handle the url itself
            return true
        }
    }
}