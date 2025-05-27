package com.walhalla.landing.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient.FileChooserParams
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.walhalla.landing.activity.DLog.d
import com.walhalla.landing.activity.DLog.handleException
import com.walhalla.webview.BuildConfig
import com.walhalla.webview.MyWebChromeClient
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

abstract class BaseWPresenter(protected val activity: AppCompatActivity) :

    WPresenter, MyWebChromeClient.Callback {
    protected var mUploadMessage: ValueCallback<Uri>? = null
    protected var mUploadMessages: ValueCallback<Array<Uri>>? = null
    private var mCapturedImageURI: Uri? = null

    init {
        d("@@@ CREATE @@@" + javaClass.simpleName)
    }


    override fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?) {
        mUploadMessage = uploadMsg
        d("@mUploadMessage@$mUploadMessage")
        openImageChooser()
    }

    override fun openFileChooser(filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams?
    ) {
        mUploadMessages = filePathCallback
        d("@mUploadMessages@$mUploadMessages")
        openImageChooser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            d("@_file_selected_@$data $mUploadMessage $mUploadMessages")
            if (null == mUploadMessage && null == mUploadMessages) {
                return
            }
            if (null != mUploadMessage) {
                handleUploadMessage(requestCode, resultCode, data)
            } else if (mUploadMessages != null) {
                handleUploadMessages(resultCode, data)
            }
        }
    }

    //    public static final String KEYFILEPROVIDER = ".fileprovider";
    //
    //    @SuppressLint("ObsoleteSdkInt")
    //    public static Uri getUriFromFile(Context context, File file) throws IllegalArgumentException {
    //        String APPLICATION_ID = context.getPackageName();
    //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || file.isDirectory()) {//Not use FileProvider is Directory
    //            return Uri.fromFile(file);
    //        } else {
    //            return FileProvider.getUriForFile(context, APPLICATION_ID + KEYFILEPROVIDER, file);
    //        }
    //    }
    //    private Uri captureImageUri() {
    //        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
    //                "FolderName");
    //        if (!imageStorageDir.exists()) {
    //            boolean b = imageStorageDir.mkdirs();
    //        }
    //        File file = new File(imageStorageDir + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg");
    //        Uri tmp = Uri.fromFile(file);
    //        DLog.d("[captureImageUri]" + tmp);
    //        return tmp;
    //    }
    fun openImageChooser() {
        try {
            if (BuildConfig.DEBUG) {
                Toast.makeText(activity, "@@@", Toast.LENGTH_SHORT).show()
            }
            var photoFile: File? = null
            try {
                photoFile = create_image(activity)
            } catch (ex: IOException) {
                Log.e("@@@", "photoFile file creation failed", ex)
            }
            //mCapturedImageURI = getUriFromFile(activity, photoFile);
            mCapturedImageURI = Uri.fromFile(photoFile)

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.setType("image/*")
            //i.setType("image/*");
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            val chooserIntent = Intent.createChooser(i, "Image Chooser")
            chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                arrayOf<Parcelable>(takePictureIntent)
            )
            activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    //api 35 content://media/picker_get_content/0/com.android.providers.media.photopicker/media/45
    private fun handleUploadMessages(resultCode: Int, intent: Intent?) {
        d("----->>>>" + (resultCode == Activity.RESULT_CANCELED))
        var results: Array<Uri> = emptyArray()
        try {
            if (resultCode != Activity.RESULT_OK) {
                results = emptyArray()


                if ( /*resultCode == Activity.RESULT_OK &&*/mCapturedImageURI != null) {
                    //file:/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_202938_4605351355341258236.jpg

//                Uri[] results = new Uri[]{
//                        Uri.fromFile(new File("/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_200744_5778639102896116151.jpg"))
//                };

                    if (mCapturedImageURI != null) {
                        results = arrayOf(mCapturedImageURI!!)
                    }


                    //            results = new Uri[]{
//                    UriUtils.getUriFromFile(this, new File("/storage/emulated/0/Pictures/img_20240811_194321_207750622564058918.jpg"))
//            };

//            results = new Uri[]{
//                    Uri.fromFile(new File("/storage/emulated/0/Pictures/img_20240811_194321_207750622564058918.jpg"))
//            };

//            results = new Uri[]{
//                    Uri.fromFile(new File("/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_200744_5778639102896116151.jpg"))
//            };
                    //content://com.android.providers.media.documents/document/image%3A3060  -- Work
                    d("@www@" + results.contentToString() + "@@" + mCapturedImageURI)
                }
            } else {
                if (intent != null) {
                    val dataString = intent.dataString
                    val clipData = intent.clipData
                    if (clipData != null) {
                        results = emptyArray()
                        for (i in 0 until clipData.itemCount) {
                            val item = clipData.getItemAt(i)
                            results[i] = item.uri
                        }
                    }
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                } else {
                    //ooo
                    d("===================$mCapturedImageURI")
                    mCapturedImageURI?.let {
                        results = arrayOf(it)
                    }
                }
            }
        } catch (e: Exception) {
            handleException(e)
        }

        //[content://com.android.providers.media.documents/document/image%3A3060,
        // content://com.android.providers.media.documents/document/image%3A3061]
        d("@@" + results.contentToString())
        mUploadMessages!!.onReceiveValue(results)
        mUploadMessages = null
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun handleUploadMessage(requestCode: Int, resultCode: Int, data: Intent?) {
        d("@@")
        var result: Uri? = null
        try {
            result = if (resultCode != Activity.RESULT_OK) {
                null
            } else {
                // retrieve from the private variable if the intent is null
                if (data == null) mCapturedImageURI else data.data
            }
        } catch (e: Exception) {
            handleException(e)
        }
        mUploadMessage!!.onReceiveValue(result)
        mUploadMessage = null

        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            result = null

            try {
                if (resultCode != Activity.RESULT_OK) {
                    result = null
                } else {
                    // retrieve from the private variable if the intent is null
                    result = if (data == null) mCapturedImageURI else data.data

                    //ooo
                    d("===========@@========$mCapturedImageURI")
                }
            } catch (e: Exception) {
                handleException(e)
                Toast.makeText(activity, "activity :$e", Toast.LENGTH_LONG).show()
            }
            mUploadMessage?.onReceiveValue(result)
            mUploadMessage = null
        }
    }

    // end of code for all versions except of Lollipop

    companion object {
        const val FILECHOOSER_RESULTCODE: Int = 1

        /*-- creating new image file here --*/
        @Throws(IOException::class)
        fun create_image(context: Context?): File {
            @SuppressLint("SimpleDateFormat") val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(
                    Date()
                )
            val imageFileName = "img_" + timeStamp + "_"
            val storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(imageFileName, ".jpg", storageDir)
        }
    }
}
