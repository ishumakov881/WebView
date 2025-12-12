package net.walhalla.landing.activity

import android.content.Intent
import androidx.appcompat.UWView
import com.walhalla.webview.ChromeView

interface WPresenter {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun a123(
        chromeView: ChromeView?, webView: UWView? //, RelativeLayout child, RelativeLayout browser
    )

    fun onConfirmation__(allowed: Boolean, resources: Array<String?>?)
}
