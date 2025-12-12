package net.walhalla.landing.activity

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.lds.webview.BuildConfig

import java.text.SimpleDateFormat
import java.util.Date
import java.util.zip.ZipFile

object DLog {
    private val DEBUG = BuildConfig.DEBUG
    private const val TAG = "@@@"


    /**
     * Log Level Error
     */
    fun e(message: String) {
        if (DEBUG) {
            Log.e(TAG, buildLogMsg(message))
        }
    }

    /**
     * Log Level Warning
     */
    fun w(message: String) {
        if (DEBUG) Log.w(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Information
     */
    fun i(message: String) {
        if (DEBUG) Log.i(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Debug
     */
    @JvmStatic
    fun d(message: String) {
        if (DEBUG) Log.d(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Verbose
     */
    fun v(message: String) {
        if (DEBUG) d(buildLogMsg(message))
    }


    private fun buildLogMsg(message: String): String {
        val ste = Thread.currentThread().stackTrace[4]


        //Class<? extends StackTraceElement> obj = ste.getClass();
        //if (ste.getClass() instanceof Fragment) {
        //    sb.append("#");
        //}
        return "\uD83D\uDE80 " +
                ste.fileName.replace(".java", "") +
                "::" +
                ste.methodName +
                " ● " +
                message +
                " █"
    }


    fun nonNull(o: Any?): Boolean {
        if (DEBUG) {
            Log.i(TAG, "nonNull: " + (o?.toString() ?: o))
        }
        return o != null
    }

    //    context.getString(R.string.app_name) + " v"
    // + versionName + " (build " + versionCode + ")"
    fun getAppVersion(context: Context): String {
        var tmp: String? = null
        try {
            //            versionCode = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(),
//                            0).versionCode;

            tmp = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (ignore: PackageManager.NameNotFoundException) {
        }
        if (tmp == null) {
            tmp = "Unknown"
        }
        return tmp
    }

    fun timeStamp(context: Context): String {
        try {
            val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
            val zf = ZipFile(applicationInfo.sourceDir)
            val ze = zf.getEntry("classes.dex")
            val time = ze.time
            val s = SimpleDateFormat.getInstance().format(Date(time))
            zf.close()
            return s
        } catch (e: Exception) {
            return "Unknown"
        }
    }

    @JvmStatic
    fun handleException(e: Exception?) {
        if (e == null) {
            d("Exception --> NULL")
            return
        }
        //        if(e instanceof IllegalStateException){
//            IllegalStateException e1 = (IllegalStateException) e;
//        }
        if (DEBUG) {
            //Log.d(TAG, e.getClass().getSimpleName());
            Log.d(
                TAG, buildLogMsg(
                    (e.javaClass.simpleName
                            + " @@@ " + (if (e.message == null) "NULL" else (e.message //.replace("Column{", "\n|_")
                            //.replace("}, ", "\n")
                            //.replace(", ", "|")
                            )
                            ))
                )
            )
        }
    } //    private static String ff(String replace) {
    //
    //        if (replace.contains("Found:")) {
    //            String[] mm = replace.replace("Expected:", "").split("Found:");
    //            String[] c = mm[0].split(",");
    //            String[] c1 = mm[1].split(",");
    //            for (int i = 0; i < c.length; i++) {
    //                if (!c[i].equals(c1[i])) {
    //                    DLog.d(c[i] + " __> " + c1[i]);
    //                }
    //            }
    //            return mm[0] + "\n" + mm[1];
    //        }
    //        return replace;
    //    }
}