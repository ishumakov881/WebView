package com.walhalla.webview

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

object WVTools {
    fun extractDomain(urlString: String?): String? {
        try {
            val url = URL(urlString)
            return url.host
        } catch (e: MalformedURLException) {
            return null
        }
    }

    fun loadBlockedDomains(context: Context, blocked_domains: Int): List<String> {
        val blockedDomains: MutableList<String> = ArrayList()
        val inputStream = context.resources.openRawResource(blocked_domains)
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                blockedDomains.add(line!!.trim()) // line не null, safe to use
            }
        } catch (ignored: IOException) {
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (ignored: IOException) {
                }
            }
        }

        return blockedDomains
    }

    fun copyToClipboard0(activity: Context, value: String) {
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("packageName", "" + value)
        clipboard.setPrimaryClip(clip)
        val tmp = String.format(activity.getString(R.string.data_to_clipboard), value)

        //        Toasty.custom(activity, tmp,
//                ComV19.getDrawable(activity, R.drawable.ic_info),
//                ContextCompat.getColor(activity, R.color.colorPrimaryDark),
//                ContextCompat.getColor(activity, R.color.white), Toasty.LENGTH_SHORT, true, true).show();
        Toast.makeText(activity, tmp, Toast.LENGTH_SHORT).show()
    }

    /**
     * Open in Other app or Cope to buffer
     *
     * @param activity
     * @param extra
     */
    fun shareText(activity: Context, extra: String?) {
        val appName = activity.getString(R.string.app_name)

        //        String extra = txt.text;
//        if (!TextUtils.isEmpty(txt.author)) {
//            extra = extra + "\n" + "— " + txt.author + "\n" + appName;
//        }

//        extra = extra + "\n "
//                + GOOGLE_PLAY_CONSTANT
//                + activity.getPackageName();
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, extra)
        intent.putExtra("com.pinterest.EXTRA_DESCRIPTION", extra)
        intent.putExtra(Intent.EXTRA_SUBJECT, appName)
        activity.startActivity(Intent.createChooser(intent, "Manifest Explorer"))
    }

    fun hideKeyboardFrom(context: Context, view: View?) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
