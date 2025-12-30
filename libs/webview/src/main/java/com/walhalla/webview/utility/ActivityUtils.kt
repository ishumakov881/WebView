package com.walhalla.webview.utility

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri

object ActivityUtils {

    //http://sberpay://invoicing/v2?bankInvoiceId=dce389134f664d90811a1196282ddd47&operationType=Web2App"
    fun openBrowser(context: Context, data: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, data.toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show()
        }
    }


    fun startEmailActivity(activity: Context, email: String, subject: String?, text: String?) {
        try {
            val builder = "mailto:$email"
            val intent = Intent(Intent.ACTION_SENDTO, builder.toUri())
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "can't start activity: $text", Toast.LENGTH_LONG).show()
        }
    }


    fun startCallActivity(activity: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, url.toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "can't start activity: $url", Toast.LENGTH_LONG).show()
        }
    }


    fun startSmsActivity(activity: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO, url.toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "can't start activity: $url", Toast.LENGTH_LONG).show()
        }
    }


    fun startMapSearchActivity(activity: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "can't start activity: $url", Toast.LENGTH_LONG).show()
        }
    }

    fun startMapYandex(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "can't start activity: $url", Toast.LENGTH_LONG).show()
        }
    }

    fun startyandexnavi(activity: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "can't start activity: $url", Toast.LENGTH_LONG).show()
        }
    }

    fun startShareActivity(activity: Activity, subject: String?, text: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "can't start activity: $text", Toast.LENGTH_LONG).show()
        }
    }


    fun starttg(activity: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger")));
            //
            Toast.makeText(activity, "can't start activity: $url", Toast.LENGTH_LONG).show()
        }
    }
}
