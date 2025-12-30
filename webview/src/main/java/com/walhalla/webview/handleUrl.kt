package com.walhalla.webview

import android.content.ActivityNotFoundException
import android.content.Intent
import android.webkit.WebView
import android.widget.Toast
import androidx.core.net.MailTo
import androidx.core.net.ParseException
import androidx.core.net.toUri
import com.walhalla.webview.CustomWebViewClient.Companion.TAG
import com.walhalla.webview.utility.ActivityUtils
import com.walhalla.webview.utility.DownloadUtility


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

fun handleUrl(customWebViewClient: CustomWebViewClient, view: WebView, url: String): Boolean {

    val var0 = customWebViewClient.isDownloadableFile(url)
    val context = view.context
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


        var openInExternalApp = customWebViewClient.isLinkExternal(url) //openInExternalApp app
        val internal = DownloadUtility.isLinkInternal(url) //internal webView
        if (!openInExternalApp && !internal) {
            openInExternalApp = WebViewAppConfig.OPEN_LINKS_IN_EXTERNAL_BROWSER
        }
        //My new Code
        if (url.endsWith(".apk")) {
            ActivityUtils.openBrowser(context, url)
            return true
        }

        // open the link
        if (openInExternalApp) {
            println(CustomWebViewClient.TAG + "@@@")
            ActivityUtils.openBrowser(context, url)
            return true
        } else {
            if (customWebViewClient.isCheckSameDomainEnabled) {
                if (isSameDomain(url, customWebViewClient.homeDomain9)) {
                    println(
                        CustomWebViewClient.TAG + "NOT_OVERRIDE:isSameDomain: ${customWebViewClient.homeDomain9} :: $url"
                    )
                    return false
                } else {
                    println(
                        CustomWebViewClient.TAG + "blocked: $url, ${customWebViewClient.homeDomain9}"
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
                println(CustomWebViewClient.TAG + "NOT_OVERRIDE: ... $url")
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
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
        sendIntent.type = "text/plain"
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
        if (CustomWebViewClient.isConnected) {
            // return false to let the WebView handle the URL
            return false
        } else {
            // show the proper "not connected" message
            view.loadData(CustomWebViewClient.offlineMessageHtml, "text/html", "utf-8")
            // return true if the host application wants to leave the current
            // WebView and handle the url itself
            return true
        }
    }
}