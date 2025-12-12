@file:Suppress("DEPRECATION")

package com.walhalla.webview.utility

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.walhalla.webview.WebViewAppConfig
import java.util.Locale
import androidx.core.net.toUri

object DownloadUtility {
    @SuppressLint("ObsoleteSdkInt")
    fun downloadFile(context: Context, url: String, fileName: String?) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            try {
                val request = DownloadManager.Request(url.toUri())

                // set download directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

                // when downloading music and videos they will be listed in the player
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    // До Android 10 можно использовать allowScanningByMediaScanner
                    request.allowScanningByMediaScanner();
                } else {
                    // Для Android 10+ помечаем файл для медиа-сканера
                    request.addRequestHeader("media-scanner", "true");
                }
                // Устанавливаем видимость загружаемого файла      // notify user when download is completed
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


                // start download
                val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                manager.enqueue(request)
            } catch (e: SecurityException) {
                //api 24 need permission


                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                No permission to write to /storage/emulated/0/Download/rules.pdf:
//                 Neither user 10091 nor current process has android.permission.WRITE_EXTERNAL_STORAGE.
                //DLog.d("@@" + e.toString() + e.getLocalizedMessage());
            } catch (e: Exception) {
                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                No permission to write to /storage/emulated/0/Download/rules.pdf:
//                 Neither user 10091 nor current process has android.permission.WRITE_EXTERNAL_STORAGE.
                //DLog.d("@@" + e.toString() + e.getLocalizedMessage());
            }
        } else {
            try {
                if (url != null) {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                }
            } catch (e: ActivityNotFoundException) {
                // can't start activity
            }
        }
    }


    fun isLinkInternal(url: String): Boolean {
        for (rule in WebViewAppConfig.LINKS_OPENED_IN_INTERNAL_WEBVIEW) {
            if (url.contains(rule)) return true
        }
        return false
    }

    fun getFileName(url: String): String {
        var url = url
        var index = url.indexOf("?")
        if (index > -1) {
            url = url.substring(0, index)
        }
        url = url.lowercase(Locale.getDefault())

        index = url.lastIndexOf("/")
        return if (index > -1) {
            url.substring(index + 1)
        } else {
            System.currentTimeMillis().toString()
        }
    }
}
