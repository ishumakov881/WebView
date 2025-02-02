package com.walhalla.landing.activity;

import android.content.Intent;
import android.widget.RelativeLayout;

import androidx.appcompat.UWView;

import com.walhalla.webview.ChromeView;

public interface WPresenter {


    void onActivityResult(int requestCode, int resultCode, Intent data);

    void a123(ChromeView chromeView, UWView webView
    //, RelativeLayout child, RelativeLayout browser
     );

    void onConfirmation__(boolean allowed, String[] resources);
}
