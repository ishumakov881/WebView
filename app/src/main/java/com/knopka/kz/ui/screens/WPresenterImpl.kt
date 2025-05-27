//package com.knopka.kz.ui.screens
//
//import android.R
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.DialogInterface
//import android.graphics.Color
//import android.net.Uri
//import android.os.Build
//import android.os.Environment
//import android.os.Handler
//import android.os.Looper
//import android.os.Message
//import android.webkit.CookieManager
//import android.webkit.PermissionRequest
//import android.webkit.ValueCallback
<<<<<<< HEAD
=======
//import android.webkit.WebChromeClient
>>>>>>> 0623d9d (Swipe Fixed..)
//import android.webkit.WebSettings
//import android.webkit.WebView
//import android.webkit.WebView.WebViewTransport
//import android.webkit.WebViewClient
//import androidx.activity.result.ActivityResult
<<<<<<< HEAD
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.DialogFragment
//import com.lds.webview.BuildConfig
//import com.walhalla.landing.activity.BaseWPresenter
//import com.walhalla.landing.activity.DLog.d
//
//import com.walhalla.webview.ChromeView
//import com.walhalla.webview.CustomWebViewClient
//import com.walhalla.webview.FullscreenWebChromeClient
//import com.walhalla.webview.UWView
//import java.io.File
//
//class WPresenterImpl(
//    handler: Handler?,
//    activity: AppCompatActivity /*, WebActivityInterface webActivity*/
//) :
//    BaseWPresenter(activity), FullscreenWebChromeClient.Callback {
//    //private final WebActivityInterface webActivity;
//    //protected ActivityResultLauncher<Intent> requestSelectFileLauncher0;
//    //protected ActivityResultLauncher<Intent> requestFileChooser;
//    protected var uploadMessage: ValueCallback<Array<Uri>>? = null
//    private var var0: CustomWebViewClient? = null
//
//
//    private var mPermissionRequest: PermissionRequest? = null
//    var j: Int = 0
//
//    private val myWebChromeClient = object : FullscreenWebChromeClient(this@WPresenterImpl) {
=======
//import androidx.appcompat.UWView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.DialogFragment
//import com.walhalla.landing.activity.BaseWPresenter
//import com.walhalla.landing.activity.DLog.d
//import com.walhalla.webview.BuildConfig
//import com.walhalla.webview.ChromeView
//import com.walhalla.webview.CustomWebViewClient
//import com.walhalla.webview.MyWebChromeClient
//import java.io.File
//
//class WPresenterImpl(
//    handler: Handler?,
//    activity: AppCompatActivity /*, WebActivityInterface webActivity*/
//) :
//    BaseWPresenter(activity), MyWebChromeClient.Callback {
//    //private final WebActivityInterface webActivity;
//    //protected ActivityResultLauncher<Intent> requestSelectFileLauncher0;
//    //protected ActivityResultLauncher<Intent> requestFileChooser;
//    protected var uploadMessage: ValueCallback<Array<Uri>>? = null
//    private var var0: CustomWebViewClient? = null
//
//
//    private var mPermissionRequest: PermissionRequest? = null
//    var j: Int = 0
//
//    private val myWebChromeClient = object : MyWebChromeClient(this@WPresenterImpl) {
>>>>>>> 0623d9d (Swipe Fixed..)
//
//        override fun onPermissionRequest(request: PermissionRequest) {
//            //mPermissionRequest = request;
//
//            val requestedResources = request.resources
//
//            //            for (String r : requestedResources) {
////                if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
////                    FragmentManager m = activity.getSupportFragmentManager();
////                    // In this sample, we only accept video capture request.
////                    ConfirmationDialogFragment
////                            .newInstance(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE})
////                            .show(m, FRAGMENT_DIALOG);
////                    break;
////                }
////            }
////
////                for (String r : requestedResources) {
////                    if (r.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
////                        FragmentManager m = activity.getSupportFragmentManager();
////                        // In this sample, we only accept video capture request.
////                        ConfirmationDialogFragment
////                                .newInstance(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE})
////                                .show(m, FRAGMENT_DIALOG);
////                        break;
////                    }
////                }
////
//////                FragmentManager m = activity.getSupportFragmentManager();
//////                // In this sample, we only accept video capture request.
//////                ConfirmationDialogFragment
//////                        .newInstance(requestedResources)
//////                        .show(m, FRAGMENT_DIALOG);
////
////                //[android.webkit.resource.VIDEO_CAPTURE, android.webkit.resource.AUDIO_CAPTURE]
////                //[android.webkit.resource.VIDEO_CAPTURE]
////
////                // Manifest.permission.CAMERA
////                //Manifest.permission.RECORD_AUDIO
//            Handler(Looper.getMainLooper()).postDelayed({
//                d("[" + j + "] " + request.resources.contentToString() + " " + request.origin)
//                //                String[] arr = new String[]{
////                        PermissionRequest.RESOURCE_AUDIO_CAPTURE,
////                        PermissionRequest.RESOURCE_VIDEO_CAPTURE
////                };
////                request.grant(arr);
////            request.grant(request.getResources());
//                if (j == 0) {
////                String[] arr = new String[]{
////                        PermissionRequest.RESOURCE_AUDIO_CAPTURE,
////                        PermissionRequest.RESOURCE_VIDEO_CAPTURE
////                };
//                    request.grant(request.resources)
//                } else {
//                    request.grant(request.resources)
//                }
//                j++
//            }, 1000)
//
//
//            //            for (int i = 0; i < 100; i++) {
////                try {
////                    String[] arr = new String[]{
////                            PermissionRequest.RESOURCE_AUDIO_CAPTURE,
////                    };
////                    request.grant(arr);
////                } catch (java.lang.IllegalStateException e) {
////
////                }
////            }
//
////                activity.runOnUiThread(() -> {
//////
//////                    Toast.makeText(activity, Arrays.toString(request.getResources()), Toast.LENGTH_SHORT).show();
//////                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//////                            && ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//////                        request.grant(request.getResources());
//////                    } else {
//////                        request.deny();
//////                    }
////                });
//        }
//
//        override fun onPermissionRequestCanceled(request: PermissionRequest) {
//            //super.onPermissionRequestCanceled(request);
//            mPermissionRequest = null
//            val fragment =
//                activity.supportFragmentManager.findFragmentByTag(FRAGMENT_DIALOG) as DialogFragment?
//            fragment?.dismiss()
//            d("@@@@@@@@@@@@====$request")
//        }
//
//        //Requre - > a.setSupportMultipleWindows(true);
//        @SuppressLint("SetJavaScriptEnabled")
//        override fun onCreateWindow(
//            view: WebView,
//            isDialog: Boolean,
//            isUserGesture: Boolean,
//            resultMsg: Message
//        ): Boolean {
//            d("@@@$isDialog")
//            if (BuildConfig.DEBUG) {
//                view.setBackgroundColor(Color.YELLOW)
//            }
//            val newWebView = WebView(view.context)
//            newWebView.settings.javaScriptEnabled = true
//
//
//            //uwView.addView(newWebView);
//            val builder = AlertDialog.Builder(view.context)
//
//            //builder.setTitle("@");
//            builder.setView(newWebView)
//            builder.setPositiveButton(
//                R.string.cancel
//            ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
//            val dialog = builder.create()
//            newWebView.webChromeClient = this
//            newWebView.webViewClient = object : WebViewClient() {
//                //                    @Override
//                //                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                //                        try {
//                //                            String newUrl = request.getUrl().toString();
//                //                            //Log.d("@@@w@@@", "shouldOverrideUrlLoading: " + transport + ", " + isDialog);
//                //                            Log.d("@@@w@@@", "shouldOverrideUrlLoading: " + newUrl);
//                //                            //dialog.setTitle(newUrl);
//                //                            view.loadUrl(newUrl);
//                //                            if (newUrl.contains("oauth.telegram.org/auth/push?")) {
//                //                                dialog.dismiss();
//                //                            }
//                //                        }catch (Exception e){
//                //                            DLog.handleException(e);
//                //                        }
//                //                        return true;
//                //                    }
//            }
//            dialog.show()
//            val transport = resultMsg.obj as WebViewTransport
//            transport.webView = newWebView
//            resultMsg.sendToTarget()
//
//            //                WebViewDialogFragment dialogFragment = new WebViewDialogFragment();
////                if (activity instanceof AppCompatActivity) {
////                    FragmentManager m = ((AppCompatActivity) activity).getSupportFragmentManager();
////                    dialogFragment.show(m, "WebViewDialogFragment");
////                }
//            return true
//        }
//    }
//
//    private fun makeFileSelector21_x(uwView: UWView) {
//        //        uwView.setWebChromeClient(@@new WebChromeClient() {
////            // For 3.0+ Devices (Start)
////            // onActivityResult attached before constructor
////            private void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
////                openImageChooser(uploadMsg, acceptType);
////            }
////
////            // For Lollipop 5.0+ Devices
////            @Override
////            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
////                openImageChooser(webView, filePathCallback, fileChooserParams);
////                return true;
////            }
////
////            // openFileChooser for Android < 3.0
////
////            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
////                openFileChooser(uploadMsg, "");
////            }
////
////            //openFileChooser for other Android versions
////
////            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
////                openFileChooser(uploadMsg, acceptType);
////            }
////        });
//
//        uwView.webChromeClient = myWebChromeClient
//    }
//
//    override fun onConfirmation__(allowed: Boolean, resources: Array<String>) {
////        DLog.d("Permission granted." + allowed + " " + Arrays.toString(resources));
////
////        if (allowed) {
////            mPermissionRequest.grant(resources);
////        } else {
////            mPermissionRequest.deny();
////            Log.d(TAG, "Permission request denied.");
////        }
////        mPermissionRequest = null;
//    }
//
<<<<<<< HEAD
//
//
=======
>>>>>>> 0623d9d (Swipe Fixed..)
//    //    public void openImageChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
//    //        if (uploadMessage != null) {
//    //            uploadMessage.onReceiveValue(null);
//    //            uploadMessage = null;
//    //        }
//    //        uploadMessage = filePathCallback;
//    //        Intent intent = null;
//    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//    //            intent = fileChooserParams.createIntent();
//    //            if (fileChooserParams.getAcceptTypes() != null) {
//    //                intent.putExtra(Intent.EXTRA_MIME_TYPES, fileChooserParams.getAcceptTypes());
//    //            }
//    //        }
//    //        try {
//    //            requestSelectFileLauncher0.launch(intent);
//    //            // activity.startActivityForResult(intent, REQUEST_SELECT_FILE);
//    //        } catch (ActivityNotFoundException e) {
//    //            uploadMessage = null;
//    //            //return false;
//    //        }
//    //    }
//    //    public void openImageChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//    //        mUploadMessage = uploadMsg;
//    //        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//    //        intent.addCategory(Intent.CATEGORY_OPENABLE);
//    //        intent.setType("*/*"); //intent.setType("image/*");//"image/*"
//    //        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//    //        //activity.startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
//    //        requestFileChooser.launch(Intent.createChooser(intent, "File Browser"));
//    //    }
//    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
//    override fun a123(chromeView: ChromeView, mView: UWView) {
//        val settings = mView.settings
//        settings.javaScriptEnabled = true
//        settings.setSupportZoom(false)
//        settings.defaultTextEncodingName = "utf-8"
//        settings.loadWithOverviewMode = true
//
//        //settings.setUseWideViewPort(true);
//
//        //ERR_TOO_MANY_REDIRECTS
//        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        //settings.setAppCacheMaxSize( 100 * 1024 * 1024 ); // 100MB
//        settings.loadsImagesAutomatically
//        settings.setGeolocationEnabled(true)
//
//        settings.domStorageEnabled = true
//        settings.builtInZoomControls = false //!@@@@@@@@@@
//        // Отключаем поддержку WebRTC
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            //disableWebRTC(settings);
//        }
//
//
//        settings.setSupportMultipleWindows(true)
//        settings.javaScriptCanOpenWindowsAutomatically = true
//
//
//        //setMediaPlaybackRequiresUserGesture - это метод, который определяет, требуется ли пользовательское взаимодействие (например, клик) для начала воспроизведения медиа (видео или аудио) в WebView. Установка значения false позволяет воспроизводить медиафайлы автоматически без необходимости взаимодействия пользователя.
//        settings.mediaPlaybackRequiresUserGesture =
//            false //Для разруливания Permission setMediaPlaybackRequiresUserGesture == false
//
//        settings.pluginState = WebSettings.PluginState.ON
//        settings.allowFileAccess = true
//        settings.allowContentAccess = true
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            settings.allowFileAccessFromFileURLs = true
//            settings.allowUniversalAccessFromFileURLs = true
//        }
//
//        //
//        //System.getProperty("http.agent")
//        setCustomUserAgent(settings)
//        //settings.setUserAgentString(...);
//        if (BuildConfig.DEBUG) {
//            //@@@ mView.setBackgroundColor(Color.parseColor("#80770000"));
//        }
//        var0 = CustomWebViewClient(mView, chromeView, activity)
//        mView.webViewClient = var0!!
//        makeFileSelector21_x(mView)
//
//        CookieManager.getInstance().setAcceptCookie(true)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CookieManager.getInstance().setAcceptThirdPartyCookies(mView, true)
//        }
//
//        //        __mView.addJavascriptInterface(
////                new MyJavascriptInterface(CompatActivity.this, __mView), "JSInterface");
////@@        mView.addJavascriptInterface(new MyJavascriptInterface(mView.getContext(), mView), "Client");
//    }
//
//    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    //    private void disableWebRTC(WebSettings webSettings) {
//    //        webSettings.setMediaPlaybackRequiresUserGesture(true); // Не автоматически воспроизводить медиа
//    //        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // Запрещаем открывать окна
//    //    }
//    private fun setCustomUserAgent(a: WebSettings) {
////        String agentString = a.getUserAgentString();
////        String agentStringNew = agentString.replace("; wv)", ")");
////        //String realFirefox = "Mozilla/5.0 (Android 14; Mobile; rv:122.0) Gecko/122.0 Firefox/122.0";
////        a.setUserAgentString(agentStringNew);
//
//
//        val realFirefox = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0"
//        a.userAgentString = realFirefox
//    }
//
//
//    //public static final int REQUEST_SELECT_FILE = 100;
//    //    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//    //            if (requestCode == REQUEST_SELECT_FILE) {
//    //                if (uploadMessage == null) return;
//    //                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
//    //                uploadMessage = null;
//    //            }
//    //        } else if (requestCode == FILECHOOSER_RESULTCODE) {
//    //            if (null == mUploadMessage) return;
//    //            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
//    //            mUploadMessage.onReceiveValue(result);
//    //            mUploadMessage = null;
//    //        }
//    //    }
<<<<<<< HEAD
////    fun loadUrlWithClearHistory(mView: UWView, s: String) {
////        mView.stopLoading()
////        mView.clearHistory()
////        mView.loadUrl("about:blank")
////        //mView.loadUrl("file:///android_asset/infAppPaused.html");
////        var0!!.resetAllErrors()
////        var0!!.setHomeUrl(s)
////        mView.loadUrl(s)
////    }
=======
//    fun loadUrlWithClearHistory(mView: UWView, s: String) {
//        mView.stopLoading()
//        mView.clearHistory()
//        mView.loadUrl("about:blank")
//        //mView.loadUrl("file:///android_asset/infAppPaused.html");
//        var0!!.resetAllErrors()
//        var0!!.setHomeUrl(s)
//        mView.loadUrl(s)
//    }
>>>>>>> 0623d9d (Swipe Fixed..)
//
//    private fun alert(result: ActivityResult, selectedFiles: Array<Uri>?, context: Context) {
//        val intent = result.data
//        val sb = StringBuilder()
//        if (selectedFiles != null) {
//            for (uri in selectedFiles) {
//                sb.append(uri.toString()).append("\n")
//            }
//        }
//        if (intent != null) {
//            sb.append(intent)
//        }
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("debug" + (if ((selectedFiles == null)) selectedFiles else selectedFiles.size))
//
//            .setMessage(sb.toString()).setPositiveButton(
//                context.getString(R.string.ok)
//            ) { dialog: DialogInterface, id: Int ->
//                dialog.cancel()
//            }
//        val alert = builder.setCancelable(false).create()
//        alert.show()
//    }
//
//
//    override fun onProgressChanged(progress: Int) {
//    }
//
<<<<<<< HEAD
=======
//    override fun openFileChooser(uploadMsg: ValueCallback<Uri>, s: String?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun openFileChooser(
//        filePathCallback: ValueCallback<Array<Uri>>,
//        fileChooserParams: WebChromeClient.FileChooserParams?
//    ) {
//        TODO("Not yet implemented")
//    }
//
>>>>>>> 0623d9d (Swipe Fixed..)
//
//    private fun captureImageUri(): Uri {
//        val imageStorageDir = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//            "FolderName"
//        )
//        if (!imageStorageDir.exists()) {
//            val b = imageStorageDir.mkdirs()
//        }
//        val file =
//            File(imageStorageDir.toString() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg")
//        val tmp = Uri.fromFile(file)
//        d("[captureImageUri]$tmp")
//        return tmp
//    }
//
//
//    companion object {
//        private const val FRAGMENT_DIALOG = "dialog"
//    }
//}
