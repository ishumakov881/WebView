package com.walhalla.landing.activity;

import android.content.Intent;


import com.walhalla.webview.ChromeView;
import com.walhalla.webview.UWView;

public interface WPresenter {


    void onActivityResult(int requestCode, int resultCode, Intent data);

    void a123(ChromeView chromeView, UWView webView
    //, RelativeLayout child, RelativeLayout browser
     );

    void onConfirmation__(boolean allowed, String[] resources);
}
