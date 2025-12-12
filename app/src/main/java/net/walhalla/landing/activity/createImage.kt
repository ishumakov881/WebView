package net.walhalla.landing.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/*-- creating new image file here --*/
@Throws(IOException::class)
fun createImage(context: Context?): File {
    @SuppressLint("SimpleDateFormat") val timeStamp =
        SimpleDateFormat("yyyyMMdd_HHmmss").format(
            Date()
        )
    val imageFileName = "img_" + timeStamp + "_"
    val storageDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}