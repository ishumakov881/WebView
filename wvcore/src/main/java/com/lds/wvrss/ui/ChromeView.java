package com.lds.wvrss.ui;

import android.app.Activity;

public interface ChromeView {

    void removeErrorPage();

    void onPageFinished(String url);

    void openBrowser(Activity context, String url);

    void webClientError(ReceivedError failure);

    void setErrorPage(ReceivedError receivedError);
}
