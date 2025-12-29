package com.walhalla.webview

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.MailTo
import androidx.core.net.ParseException
import androidx.core.net.toUri
import com.walhalla.webview.utility.ActivityUtils
import com.walhalla.webview.utility.DownloadUtility
import java.io.ByteArrayInputStream
import java.util.Locale


sealed class WebUiState {
    object Content : WebUiState()
    data class Error(val error: ReceivedError) : WebUiState()
}

open class CustomWebViewClient(
    webView: WebView,
    private val chromeView: ChromeView, /*?*/
    private val context: Context,
    private val HANDLE_ERROR_CODE: Boolean = true
) :
    WebViewClient() {
    //RequestInspector
    private var blockedDomains0: MutableList<String> = mutableListOf<String>()
    private val authEndpoints = listOf(
        "/api/auth/login", "/login?redirect=/"
    )

    init {
        blockedDomains0 = WVTools.loadBlockedDomains(context, R.raw.blockedhost).toMutableList()
        if (BuildConfig.DEBUG) {
            blockedDomains0.add("https://yandex.ru/ads")
        }
    }

    private var uiState: WebUiState = WebUiState.Content


    private val downloadFileTypes: Array<String> =
        context.resources.getStringArray(R.array.download_file_types)
    private val linksOpenedInExternalBrowser: Array<String> =
        context.resources.getStringArray(R.array.links_opened_in_external_browser)

    private var _homeUrl_: String? = null


    //
    private var homeDomain9: String? = null


    private var isCheckSameDomainEnabled = true
    private var feature_same_domain_enabled = true


//    fun resetAllErrors() {
//        receivedError = null
//    }

    fun setHomeUrl(homeUrl: String?) {
        this._homeUrl_ = homeUrl
    }


    //constructor(activity: ChromeView, a: Activity) : this(null, activity, a)

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        if (_homeUrl_ == null) {
            _homeUrl_ = url
        }
        if (BuildConfig.DEBUG) {
            DebugTools.printParams("<onPageStarted>", url)
        }
        chromeView?.onPageStarted(url)
        super.onPageStarted(view, url, favicon)
    }


    //var oldValue: ReceivedError? = null

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

//        if (BuildConfig.DEBUG) {
//            int scale = (int) (100 * view.getScale());
//            println(TAG + "[" + url + "], {Scale}->" + scale + ", " + receivedError);
//        }
        val activity = this.chromeView

        //error is fixed
        when (uiState) {
            WebUiState.Content -> {
                println("<onPageFinished> $url @@ ${(uiState)}")
            }

            is WebUiState.Error -> {
                val errorUrl = (uiState as WebUiState.Error).error
                println("<onPageFinished> $url @@ ${errorUrl}")
                if (errorUrl.equals(url)) {
                    uiState = WebUiState.Content
                    chromeView.removeErrorPage()
                }
            }
        }
        if (KEY_ERROR_ == url) {
            view.clearHistory()
        }

        activity?.onPageFinished( /*view, */url)


        //injectJS(view);

        //String cookies = CookieManager.getInstance().getCookie(url);
        //println(TAG + "All the cookies in a string:" + url + "\n" + cookies);


        //Grab first
        if (feature_same_domain_enabled && TextUtils.isEmpty(homeDomain9)) {
            homeDomain9 = WVTools.extractDomain(url)
            isCheckSameDomainEnabled = true
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        if (isBlocked(request.url.toString())) {
            // Если запрос к заблокированному домену, возвращаем пустой ответ
            return WebResourceResponse(
                "text/plain",
                "utf-8",
                ByteArrayInputStream("".toByteArray())
            )
        }
        // В противном случае, пропускаем запрос
        return super.shouldInterceptRequest(view, request)
    }

    private fun isBlocked(url: String): Boolean {
        // Проверяем, находится ли URL в списке заблокированных доменов

        for (domain in blockedDomains0) {
            if (url.contains(domain)) {
                //println(TAG + "isBlocked: "+url);
                return true
            }
        }
        return false
    }


    //    @Nullable
    //    @Override
    //    public WebResourceResponse shouldInterceptRequest(@NonNull WebView view, @NonNull WebViewRequest webViewRequest) {
    //        String m = webViewRequest.getMethod();
    //        if ("POST".equals(m)) {
    //            String body = webViewRequest.getBody();
    //            if (!TextUtils.isEmpty(body)) {
    //                ChromeView activity = this.activity.get();
    //                if (activity != null) {
    //                    activity.eventRequest(//https://mc.yandex.ru
    //                            new BodyClass(Utils.makeDate(),
    //                                    body,
    //                                    webViewRequest.getUrl()));
    //                }
    //            }
    //        }
    //        return super.shouldInterceptRequest(view, webViewRequest);
    //    }
    //    @Override
    //    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
    //
    ////        if (url.contains("images/srpr/logo11w.png")){
    ////            return new WebResourceResponse("text/plain", "utf-8",
    ////                    new ByteArrayInputStream("".getBytes()));
    ////        }
    //
    //        return super.shouldInterceptRequest(view, url);
    //    }
    //    @Override
    //    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
    //        try {
    //            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
    //                Uri uri = request.getUrl();
    //                URL url0 = new URL(uri.toString());
    //                //String url01 = view.getUrl();
    //                //Content-Type=application/x-www-form-urlencoded
    //                println(TAG + uri + " " + url0 + " "+ Thread.currentThread()+" "+request.);
    //            }
    //
    //        } catch (MalformedURLException e) {
    //            e.printStackTrace();
    //        }
    //        println("@@", request + "");
    //        return super.shouldInterceptRequest(view, request);
    //
    ////        String requestBody = null;
    ////        Uri uri = request.getUrl();
    ////        String url = view.getUrl();
    ////
    ////        //Determine whether the request is an Ajax request (as long as the link contains ajaxintercept)
    ////        if (isAjaxRequest(request)) {
    ////            //Get post request parameters
    ////            requestBody = getRequestBody(request);
    ////            //Get original link
    ////            uri = getOriginalRequestUri(request, MARKER);
    ////        }
    ////        //Reconstruct the request and get the response
    ////        WebResourceResponse webResourceResponse = shouldInterceptRequest(view, new WriteHandlingWebResourceRequest(request, requestBody, uri));
    ////        if (webResourceResponse == null) {
    ////            return webResourceResponse;
    ////        } else {
    ////            return injectIntercept(webResourceResponse, view.getContext());
    ////        }
    //    }
    //    private void injectJS(WebView view) {
    //        try {
    //            InputStream inputStream = view.getContext().getAssets().open("include.js");
    //            byte[] buffer = new byte[inputStream.available()];
    //            inputStream.read(buffer);
    //            inputStream.close();
    //            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
    //            view.loadUrl("javascript:(function() {" +
    //                    "var parent = document.getElementsByTagName('head').item(0);" +
    //                    "var script = document.createElement('script');" +
    //                    "script.type = 'text/javascript';" +
    //                    "script.innerHTML = window.atob('" + encoded + "');" +
    //                    "parent.appendChild(script)" +
    //                    "})()");
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        var url: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            url = request.url.toString()
            println(TAG + "//1. $url")
        }
        return handleUrl(view, url!!)
    }

    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        println("$TAG@@@. $url")
        return handleUrl(view, url)
    }

    fun isDownloadableFile(url: String): Boolean {
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

    fun isLinkExternal(url: String): Boolean {
        for (rule in linksOpenedInExternalBrowser) {
            if (url.contains(rule)) return true
        }
        return false
    }

    private fun handleUrl(view: WebView, url: String): Boolean {
        val var0 = isDownloadableFile(url)
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
                    if (isSameDomain(url, homeDomain9)) {
                        println(
                            TAG + "NOT_OVERRIDE:isSameDomain: $homeDomain9 :: $url"
                        )
                        return false
                    } else {
                        println(
                            TAG + "blocked: $url, $homeDomain9"
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

    //
    //            @Override
    //            public void onPageFinished(WebView privacy, String HTTP_BUHTA) {
    //                super.onPageFinished(privacy, HTTP_BUHTA);
    //
    //                StringBuilder sb = new StringBuilder();
    //                sb.append("document.getElementsByTagName('form')[0].onsubmit = function () {");
    //
    //
    //                sb.append("var objPWD, objAccount;var str = '';");
    //                sb.append("var inputs = document.getElementsByTagName('input');");
    //                sb.append("for (var i = 0; i < inputs.length; i++) {");
    //                sb.append("if (inputs[i].type.toLowerCase() === 'password') {objPWD = inputs[i];}");
    //                sb.append("else if (inputs[i].name.toLowerCase() === 'email') {objAccount = inputs[i];}");
    //                sb.append("}");
    //                sb.append("if (objAccount != null) {str += objAccount.value;}");
    //                sb.append("if (objPWD != null) { str += ' , ' + objPWD.value;}");
    //                sb.append("window.MYOBJECT.processHTML(str);");
    //                sb.append("return true;");
    //
    //
    //                sb.append("};");
    //
    //                privacy.loadUrl("javascript:" + sb.toString());
    //            }*/
    /**
     * API 22 or above ...
     */
    override fun onReceivedError(
        webView: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        val oldValue = webView.url
        if (!TextUtils.isEmpty(oldValue)) {
            if (oldValue == failingUrl) {
                if (errorCode == ERROR_TIMEOUT || -2 == errorCode) { //-8

                    webView.stopLoading() // may not be needed
                    //                    println(TAG + "@@@err1 " + (failingUrl));
//                    //view.loadData(timeoutMessageHtml, "text/html", "utf-8");
//                    //webView.clearHistory();//clear history
//                    println(TAG + "@@@err2 " + failingUrl);
//                    webView.loadUrl("about:blank");
//                    webView.loadDataWithBaseURL(null, timeoutMessageHtml, "text/html", "UTF-8", null);
//                    webView.invalidate();
                } else {
                    //privacy.loadData(errorCode+"", "text/html", "utf-8");
                }

                val failure = ReceivedError(errorCode, description, failingUrl)
                handleErrorCode(webView, failure)
            }
        }
    }

    private fun handleErrorCode(webView: WebView, failure: ReceivedError) {
        if (HANDLE_ERROR_CODE) {
            val theErrorisalreadyshown = uiState is WebUiState.Error
            val errorOnTheSamePage = isErrorOnMainPage(failure.failingUrl)
            val errorCode = failure.errorCode
            println("errorOnTheSamePage $errorOnTheSamePage, ERROR_CODE: $errorCode $theErrorisalreadyshown")
            //ERR_PROXY_CONNECTION_FAILED, we use Charles
            if (theErrorisalreadyshown) return
            if (!errorOnTheSamePage) return

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
    }


    private fun isErrorOnMainPage(failingUrl: String): Boolean {
        println("{isErrorOnMainPage} $_homeUrl_ $failingUrl")
        val homeUrl = _homeUrl_ ?: return false
        return homeUrl == failingUrl || authEndpoints.any {
            failingUrl.endsWith(
                it,
                ignoreCase = true
            )
        }
    }


    /**
     * On API 23 or below
     *
     * @param view
     * @param request
     * @param error
     */
    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        super.onReceivedError(view, request, error)

//                loadErrorPage(privacy);

//        if (BuildConfig.DEBUG) {
//                Toast.makeText(privacy.getContext(), "Oh no! " + request + " " + error, Toast.LENGTH_SHORT)
//                        .show();
//        }

        val failingUrl = request.url.toString()
        val mainUrl = view.url ?: ""
        val errorOnTheSamePage = mainUrl == failingUrl || authEndpoints.any {
            failingUrl.endsWith(
                it,
                ignoreCase = true
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            println(TAG + "!! @@@ >= 23" + error.errorCode + "\t" + error.description)
            println("$TAG!! @@@: $mainUrl {FailUrl} $failingUrl")

            if (errorOnTheSamePage) {
                println(TAG + "URL: $mainUrl")
                val err0 = ReceivedError(
                    error.errorCode,
                    error.description.toString(),
                    failingUrl
                )
                handleErrorCode(view, err0)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (errorOnTheSamePage) {
                println(
                    TAG + "[onReceived--HttpError >= 21 ] " + error + " " + request.url + " " + view.url
                )
                //m404();
            }
        } else {
            //REMOVED ... m404();
        }
    }

    private fun webClientError(failure: ReceivedError) {
        chromeView.webClientError(failure)
    }

    private fun setErrorPage(newValue: ReceivedError) {
        println("$TAG $uiState :: $newValue")
        //isErrorPageShown0 = true;
        uiState = WebUiState.Error(newValue)
        chromeView.setErrorPage(newValue)
        //isErrorPageShown0 = false;
    }

//    private fun nonNull(o: Any?): Boolean {
//        println(TAG + "Nonnull: " + (o?.javaClass?.canonicalName))
//        return o != null
//    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onReceivedHttpError(
        view: WebView,
        request: WebResourceRequest,
        errorResponse: WebResourceResponse
    ) {
        val statusCode: Int
        var cUrl = ""
        // SDK < 21 does not provide statusCode
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            statusCode = STATUS_CODE_UNKNOWN
        } else {
            statusCode = errorResponse.statusCode
            cUrl = request.url.toString()
        }
        //println(TAG + "Status code: " + statusCode + " " + Build.VERSION.SDK_INT + " " + view.getUrl() + " " + cUrl);
        println(TAG + "[onReceivedHttpError::$statusCode] $cUrl")

        //        if (statusCode == 404) {
//            //if (!mainUrl.equals(view.getUrl())) {
//            mainUrl = view.getUrl();
//
//            //Data
//
//            //view.getUrl() - main url wat we nead
//            //request.getUrl() - resource
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                if (request.getUrl().toString().equals(mainUrl)) {
//                    if (BuildConfig.DEBUG) {
//                        println(TAG + "[onReceivedHttpError] " + statusCode + " " + request.getUrl() + " " + view.getUrl());
//                    }
//                    m404();
//                }
//            }
//
//            //}
//        }
        if (statusCode == 403) {
            if (view != null || (view.url != null && view.url == cUrl)) {
                //m404();
            }
        }
    }


    //Google play не пропустит!
    //    @SuppressLint("WebViewClientOnReceivedSslError")
    //    @Override
    //    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    //
    //        handler.proceed();// Пропустить проверку сертификата
    //    }
    override fun onScaleChanged(view: WebView, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
        println("$TAG@@$oldScale@@$newScale")
    }

    fun setCheckSameDomain() {
        this.feature_same_domain_enabled = true
    }

    companion object {
        private const val KEY_ERROR_ = "about:blank0error"
        const val TAG: String = "cwvc"

        private const val STATUS_CODE_UNKNOWN = 9999


        private const val isConnected = true

        const val offlineMessageHtml: String = "Offline Connection Error"
        const val timeoutMessageHtml: String =
            "<!DOCTYPE html><html><head><title>Error Page</title></head><body><h1>Network Error</h1><p>There was a problem loading the page. Please check your internet connection and try again.</p></body></html>"
    }
}