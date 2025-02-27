package com.lds.wvrss.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.MailTo
import androidx.core.net.ParseException
import com.lds.webview.BuildConfig
import com.lds.webview.R
import com.walhalla.webview.ActivityUtils
import com.walhalla.webview.ChromeView
import com.walhalla.webview.DownloadUtility
import com.walhalla.webview.ReceivedError
import com.walhalla.webview.WVTools
import com.walhalla.webview.WebViewAppConfig
import java.io.ByteArrayInputStream
import java.util.Locale


open class CustomWebViewClient(
    private val sessionName: String,
    mView: WebView,
    private val chromeView: ChromeView,
    private val context: Context

) :
    WebViewClient() {
    //RequestInspector


    var receivedError: ReceivedError? = null


    private val blockedDomains0: List<String>
    private val downloadFileTypes: List<String>
    private val linksOpenedInExternalBrowser: List<String>

    init {
        blockedDomains0 = WVTools.loadBlockedDomains(context, R.raw.blockedhost)
        downloadFileTypes = context.resources.getStringArray(R.array.download_file_types).toList()
        linksOpenedInExternalBrowser = context.resources.getStringArray(R.array.links_opened_in_external_browser).toList()
    }

    private var _homeUrl_: String? = null


    //
    private var homeDomain9: String? = null


    private var isCheckSameDomainEnabled = false
    private var feature_same_domain_enabled = false


    fun resetAllErrors() {
        receivedError = null
    }

    fun setHomeUrl(homeUrl: String?) {
        this._homeUrl_ = homeUrl
    }


//    constructor(sessionName: String, activity: ChromeView, a: Activity) : this(
//        sessionName,
//        null,
//        activity,
//        a
//    )

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        if (_homeUrl_ == null) {
            _homeUrl_ = url
        }
        if (BuildConfig.DEBUG) {
            printParams("<onPageStarted>", url)
        }
        val activity = this.chromeView
        if (activity != null) {
            activity.onPageStarted(url)
        }
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(webView: WebView, url: String) {
        super.onPageFinished(webView, url)
        //webView.setInitialScale(((100*webView.getScale()).toInt()));


//        val jsCode = """
//        (function() {
//            var elements = document.querySelectorAll('.vim-preroll, .vim-overroll');
//            elements.forEach(function(el) {
//                el.remove();
//            });
//        })();
//    """.trimIndent()
//        val jsCode = """
//        (function() {
//            var elements = document.querySelectorAll('.vim-overroll');
//            elements.forEach(function(el) {
//                el.remove();
//            });
//        })();
//    """.trimIndent()
//        webView.evaluateJavascript(jsCode, null)

        // Внедряем JavaScript-код после загрузки страницы
//        val jsCode = """
//            (function() {
//                // Функция для изменения стилей элемента
//                function styleCloseBtn() {
//                    const closeBtn = document.getElementById('close-btn');
//                    if (closeBtn) {
//                        closeBtn.style.fontSize = '40px'; // Увеличиваем размер
//                        closeBtn.style.color = 'red'; // Меняем цвет на красный
//                    }
//                }
//
//                // Отслеживаем появление элемента с помощью MutationObserver
//                const observer = new MutationObserver((mutationsList) => {
//                    for (let mutation of mutationsList) {
//                        if (mutation.type === 'childList') {
//                            const closeBtn = document.getElementById('close-btn');
//                            if (closeBtn) {
//                                styleCloseBtn();
//                                observer.disconnect(); // Останавливаем наблюдение после изменения
//                            }
//                        }
//                    }
//                });
//
//                // Начинаем наблюдение за изменениями в DOM
//                observer.observe(document.body, { childList: true, subtree: true });
//            })();
//
//            """.trimIndent()
//
//
//        // Выполняем JavaScript-код в контексте WebView
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.evaluateJavascript(jsCode, null)
//        } else {
//            webView.loadUrl("javascript:$jsCode")
//        }

        if (BuildConfig.DEBUG) {
            printParams("<onPageFinished>", url)
        }
//        if (BuildConfig.DEBUG) {
//            val scale = (100 * webView.scale)
//            Log.d(TAG, ("[$url").toString() + "], {Scale}->" + scale + ", " + receivedError)
//        }
        val activity = this.chromeView

        //error is fixed
        if (oldValue != null && receivedError == null) {
            activity.removeErrorPage()
        }

        oldValue = receivedError //set
        receivedError = null //reset error

        if (KEY_ERROR_ == url) {
            webView.clearHistory()
        }

        if (activity != null) {
            activity.onPageFinished( /*view, */url)
        }


        //injectJS(view);

        //String cookies = CookieManager.getInstance().getCookie(url);
        //Log.d(TAG, "All the cookies in a string:" + url + "\n" + cookies);


        //Grab first
        if (feature_same_domain_enabled && TextUtils.isEmpty(homeDomain9)) {
            homeDomain9 = extractDomain(url)
            isCheckSameDomainEnabled = true
        }
    }

    private fun extractDomain(url: String): String? {
        return ""
    }

    fun printParams(s: String, url: String) {
        if (BuildConfig.DEBUG) {
            val uri = Uri.parse(url)
            val domain = uri.host
            Log.d(TAG, "[$sessionName] $s $url $domain")
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

            println("@@@ isBlocked: $url")


            if (url.contains(domain)) {
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
    //                Log.d(TAG, uri + " " + url0 + " "+ Thread.currentThread()+" "+request.);
    //            }
    //
    //        } catch (MalformedURLException e) {
    //            e.printStackTrace();
    //        }
    //        Log.d("@@", request + "");
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
            Log.d(TAG, "//1. $url")
        }
        return handleUrl(view, url!!)
    }

    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        Log.d(TAG, "@@@. $url")
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
        when {
            isDownloadableFile(url) -> {
                Toast.makeText(context, R.string.fragment_main_downloading, Toast.LENGTH_LONG)
                    .show()
                DownloadUtility.downloadFile(context, url, DownloadUtility.getFileName(url))
                return true
            }


            url.startsWith("http://") || url.startsWith("https://") -> {
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
//                Log.d(TAG, "@" + modifiedUrl);
//                chromeView.openOauth2(context, modifiedUrl);
//                return true;
//            }


                //Log.d(TAG, "@c@");
                // determine for opening the link externally or internally


                var openInExternalApp = isLinkExternal(url) //openInExternalApp app
                val internal: Boolean = DownloadUtility.isLinkInternal(url) //internal webView
                if (!openInExternalApp && !internal) {
                    openInExternalApp = WebViewAppConfig.OPEN_LINKS_IN_EXTERNAL_BROWSER
                }
                //My new Code
                if (url.endsWith(".apk")) {
                    chromeView.openBrowser(url)
                    return true
                }

                // open the link
                if (openInExternalApp) {
                    Log.d(TAG, "@@@")
                    chromeView.openBrowser(url)
                    return true
                } else {
                    if (isCheckSameDomainEnabled) {
                        if (isSameDomain(url, homeDomain9)) {
                            Log.d(
                                TAG,
                                "NOT_OVERRIDE:isSameDomain: $url"
                            )
                            return false
                        } else {
                            Log.d(
                                TAG,
                                "blocked: $url, $homeDomain9"
                            )
                            //url blocked
                            return true
                        }
                    } else {
                        //@@@ showActionBarProgress(true);
                        Log.d(TAG, "NOT_OVERRIDE: $url")
                        return false
                    }
                }
            }


            url.startsWith("tg:") || url.startsWith("https://t.me/") -> {
                ActivityUtils.starttg(context, url)
                return true
            }

            url.startsWith("market:") -> {
                ActivityUtils.market(context, url)
                return true
            }

            url.startsWith("intent:") -> {
                ActivityUtils.handleIntentUrl(context, url)
                return true //handle itself
            }

            url.startsWith("file:///android_asset") -> {
                Toast.makeText(context, "@@@@@@@@@", Toast.LENGTH_SHORT).show()
                return false
            }



            url.startsWith("mailto:") -> {
                try {
                    val mailTo = MailTo.parse(url)
                    ActivityUtils.startEmailActivity(
                        context,
                        mailTo.to,
                        mailTo.subject,
                        mailTo.body
                    )
                } catch (_: ParseException) {
                }
                return true
            }

            url.startsWith("tel:") -> {
                ActivityUtils.startCallActivity(context, url)
                return true
            }

            url.startsWith("sms:") -> {
                ActivityUtils.startSmsActivity(context, url)
                return true
            }

            url.startsWith("geo:") -> {
                ActivityUtils.startMapSearchActivity(context, url)
                return true
            }

            url.startsWith("yandexnavi:") -> {
                ActivityUtils.startyandexnavi(context, url)
                return true
            }
            url.contains(":") -> { // Проверяем, что это URI со схемой
                ActivityUtils.resolveUrl(context, url)
                return true
            }

            else -> {
                if (url.startsWith("intent://")) {
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
        }


    }

    private fun isSameDomain(url: String, baseDomain: String?): Boolean {
//        Uri uri = Uri.parse(url);
//        String domain = uri.getHost();
//        if (domain != null && (domain.endsWith("." + baseDomain) || domain.equals(baseDomain))) {
//            return true;
//        } else {
//            return false;
//        }
        //o.php?
        return true
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
                    //                    Log.d(TAG, "@@@err1 " + (failingUrl));
//                    //view.loadData(timeoutMessageHtml, "text/html", "utf-8");
//                    //webView.clearHistory();//clear history
//                    Log.d(TAG, "@@@err2 " + failingUrl);
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


    var oldValue: ReceivedError? = null

    private fun handleErrorCode(webView: WebView, failure: ReceivedError) {
        if (HANDLE_ERROR_CODE) {
            //ERR_PROXY_CONNECTION_FAILED, we use Charles

            val prError = (failure.errorCode == ERROR_PROXY_AUTHENTICATION)
            if (prError) {
                setErrorPage(failure)
            } else if (failure.errorCode == ERROR_HOST_LOOKUP /*ERR_INTERNET_DISCONNECTED*/) { //-2 ERR_NAME_NOT_RESOLVED
                if (!theerrorisalreadyshown()) {
                    if (isErrorOnTheSamePage(failure.failingUrl)) {
                        //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
                        //@@@ webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
                        setErrorPage(failure)
                        //Toast.makeText(context, "@@@", Toast.LENGTH_SHORT).show();
                    }
                }
                webClientError(failure)
            } else if (failure.errorCode == ERROR_TIMEOUT) { //-8 ERR_CONNECTION_TIMED_OUT
                if (!theerrorisalreadyshown()) {
                    if (isErrorOnTheSamePage(failure.failingUrl)) {
                        //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
                        //@@@ webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
                        setErrorPage(failure)
                    }
                }
                webClientError(failure)
            } else if (failure.errorCode == ERROR_CONNECT) { // -6	net::ERR_CONNECTION_REFUSED
                if (!theerrorisalreadyshown()) {
                    if (isErrorOnTheSamePage(failure.failingUrl)) {
                        //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
                        //@@@ webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
                        setErrorPage(failure)
                    }
                }
                webClientError(failure)
            } else if (failure.errorCode != -14) { // -14 is error for file not found, like 404.
                webClientError(failure)
            }
            //ERR_CONNECTION_REFUSED
            //ERR_CONNECTION_RESET
        }
    }

    private fun isErrorOnTheSamePage(failingUrl: String?): Boolean {
        return _homeUrl_ != null && _homeUrl_ == failingUrl
    }

    private fun theerrorisalreadyshown(): Boolean {
        return receivedError != null && receivedError!!.errorCode != null
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
        val failureUrl = request.url.toString()

        val mainUrl = if ((view.url == null)) "" else view.url
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "!! @@@ >= 23" + error.errorCode + "\t" + error.description)
            Log.d(
                TAG,
                "!! @@@: $mainUrl {FailUrl} $failureUrl"
            )

            if (mainUrl == failureUrl) {
                Log.d(TAG, "URL: $mainUrl")
                val err0 = ReceivedError(
                    error.errorCode,
                    error.description.toString(),
                    failureUrl
                )
                handleErrorCode(view, err0)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mainUrl == failureUrl) {
                Log.d(
                    TAG,
                    "[onReceived--HttpError >= 21 ] " + error + " " + request.url + " " + view.url
                )
                //m404();
            }
        } else {
            //REMOVED ... m404();
        }
    }

    private fun webClientError(failure: ReceivedError) {
        val view = chromeView
        if (nonNull(view)) {
            view.webClientError(failure)
        }
    }

    private fun setErrorPage(newValue: ReceivedError) {
        Log.d(TAG, "@@@@")
        //isErrorPageShown0 = true;
        receivedError = newValue
        val view = chromeView
        if (nonNull(view)) {
            view.setErrorPage(receivedError!!)
        }
        //isErrorPageShown0 = false;
    }

    private fun nonNull(o: Any?): Boolean {
        Log.d(TAG, "Nonnull" + (if ((o != null)) o.javaClass.canonicalName else null))
        return o != null
    }

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
        //Log.d(TAG, "Status code: " + statusCode + " " + Build.VERSION.SDK_INT + " " + view.getUrl() + " " + cUrl);
        Log.d(
            TAG,
            "[onReceivedHttpError::$statusCode] $cUrl"
        )

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
//                        Log.d(TAG, "[onReceivedHttpError] " + statusCode + " " + request.getUrl() + " " + view.getUrl());
//                    }
//                    m404();
//                }
//            }
//
//            //}
//        }
        if (statusCode == 403) {
            if (view.url != null && view.url == cUrl) {
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
        // Здесь вы можете получить текущий масштаб WebView
        val currentScale = newScale
        // Добавьте здесь свою логику для обработки изменения масштаба
        println("Масштаб изменился: $currentScale, OldScale: $oldScale")
    }

    fun setCheckSameDomain() {
        this.feature_same_domain_enabled = true
    }

    companion object {
        private const val KEY_ERROR_ = "about:blank0error"
        const val TAG: String = "@@@"
        private const val STATUS_CODE_UNKNOWN = 9999
        private const val HANDLE_ERROR_CODE = true

        private const val isConnected = true

        const val offlineMessageHtml: String = "Offline Connection Error"
        const val timeoutMessageHtml: String =
            "<!DOCTYPE html><html><head><title>Error Page</title></head><body><h1>Network Error</h1><p>There was a problem loading the page. Please check your internet connection and try again.</p></body></html>"
    }
}
