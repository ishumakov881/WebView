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
    val chromeView: ChromeView?,
    val context: Context,
    private val HANDLE_ERROR_CODE: Boolean = true
) :
    WebViewClient() {
    //RequestInspector
    private var blockedDomains0: MutableList<String> = mutableListOf<String>()


    init {
        blockedDomains0 = WVTools.loadBlockedDomains(context, R.raw.blockedhost).toMutableList()
        if (BuildConfig.DEBUG) {
            blockedDomains0.add("https://yandex.ru/ads")
        }
    }


    private var uiState: WebUiState = WebUiState.Content


    val downloadFileTypes: Array<String> = context.resources.getStringArray(R.array.download_file_types)
    private val linksOpenedInExternalBrowser: Array<String> =
        context.resources.getStringArray(R.array.links_opened_in_external_browser)

    private var _homeUrl_: String? = null


    //
    private var homeDomain9: String? = null


    var isCheckSameDomainEnabled = true
    private var feature_same_domain_enabled = true


    fun resetAllErrors() {
        receivedError = null
    }

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

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        if (BuildConfig.DEBUG) {
            DebugTools.printParams("<onPageFinished>", url)
        }
//        if (BuildConfig.DEBUG) {
//            int scale = (int) (100 * view.getScale());
//            println(TAG + "[" + url + "], {Scale}->" + scale + ", " + receivedError);
//        }
        val activity = this.chromeView

        //error is fixed
        if (oldValue != null && receivedError == null) {
            activity?.removeErrorPage()
        }

        oldValue = receivedError //set
        receivedError = null //reset error

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
        return  handleUrl(baseDomain = homeDomain9, view, url = request.url.toString())
    }

    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean  =  handleUrl(baseDomain = homeDomain9, view, url)


    fun isLinkExternal(url: String): Boolean {
        for (rule in linksOpenedInExternalBrowser) {
            if (url.contains(rule)) return true
        }
        return false
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
                if (HANDLE_ERROR_CODE) handleErrorCode(webView, failure)
            }
        }
    }

    private var pageHasError = false


    private fun isErrorOnTheSamePage(failingUrl: String): Boolean {
        return _homeUrl_ != null && _homeUrl_ == failingUrl
    }

    //private fun theErrorisalreadyshown(): Boolean { return receivedError != null }


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

        val mainUrl = view.url ?: ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            println(TAG + "!! @@@ >= 23" + error.errorCode + "\t" + error.description)
            println("$TAG!! @@@: $mainUrl {FailUrl} $failureUrl")

            if (mainUrl == failureUrl) {
                println(TAG + "URL: $mainUrl")
                val err0 = ReceivedError(
                    error.errorCode,
                    error.description.toString(),
                    failureUrl
                )
                if (HANDLE_ERROR_CODE) handleErrorCode(view, err0)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mainUrl == failureUrl) {
                println(
                    TAG + "[onReceived--HttpError >= 21 ] " + error + " " + request.url + " " + view.url
                )
                //m404();
            }
        } else {
            //REMOVED ... m404();
        }
    }

    fun webClientError(failure: ReceivedError) {
        if (nonNull(chromeView)) {
            chromeView?.webClientError(failure)
        }
    }

    fun setErrorPage(newValue: ReceivedError) {
        println(TAG + "$receivedError")
        //isErrorPageShown0 = true;
        receivedError = newValue

        if (nonNull(chromeView)) {
            chromeView?.setErrorPage(newValue)
        }
        //isErrorPageShown0 = false;
    }

    private fun nonNull(o: Any?): Boolean {
        println(TAG + "Nonnull: " + (o?.javaClass?.canonicalName))
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


        const val isConnected = true

        const val offlineMessageHtml: String = "Offline Connection Error"
        const val timeoutMessageHtml: String =
            "<!DOCTYPE html><html><head><title>Error Page</title></head><body><h1>Network Error</h1><p>There was a problem loading the page. Please check your internet connection and try again.</p></body></html>"
    }
}