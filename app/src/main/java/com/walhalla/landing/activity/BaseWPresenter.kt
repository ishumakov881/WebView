package com.walhalla.landing.activity;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.walhalla.webview.BuildConfig;
import com.walhalla.webview.MyWebChromeClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public abstract class BaseWPresenter implements WPresenter, MyWebChromeClient.Callback {

    public static final int FILECHOOSER_RESULTCODE = 1;
    protected final AppCompatActivity activity;

    protected ValueCallback<Uri> mUploadMessage;
    protected ValueCallback<Uri[]> mUploadMessages;
    private Uri mCapturedImageURI = null;
    public BaseWPresenter(AppCompatActivity compatActivity) {
        this.activity = compatActivity;

        DLog.d("@@@ CREATE @@@" + this.getClass().getSimpleName());
    }




    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        DLog.d("@mUploadMessage@" + mUploadMessage);
        openImageChooser();
    }

    @Override
    public void openFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        mUploadMessages = filePathCallback;
        DLog.d("@mUploadMessages@" + mUploadMessages);
        openImageChooser();
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (requestCode == FILECHOOSER_RESULTCODE) {
            DLog.d("@_file_selected_@" + data + " " + mUploadMessage + " " + mUploadMessages);
            if (null == mUploadMessage && null == mUploadMessages) {
                return;
            }
            if (null != mUploadMessage) {
                handleUploadMessage(requestCode, resultCode, data);
            } else if (mUploadMessages != null) {
                handleUploadMessages(resultCode, data);
            }
        }
    }

    /*-- creating new image file here --*/
    public static File create_image(Context context) throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
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

    public void openImageChooser() {
        try {
            if (BuildConfig.DEBUG) {
                Toast.makeText(activity, "@@@", Toast.LENGTH_SHORT).show();
            }
            File photoFile = null;
            try {
                photoFile = create_image(activity);
            } catch (IOException ex) {
                Log.e("@@@", "photoFile file creation failed", ex);
            }
            //mCapturedImageURI = getUriFromFile(activity, photoFile);
            mCapturedImageURI = Uri.fromFile(photoFile);

            final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            //i.setType("image/*");
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{takePictureIntent});
            activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    //api 35 content://media/picker_get_content/0/com.android.providers.media.photopicker/media/45

    private void handleUploadMessages(final int resultCode, final Intent intent) {
        DLog.d("----->>>>" + (resultCode == Activity.RESULT_CANCELED));
        Uri[] results = null;
        try {
            if (resultCode != Activity.RESULT_OK) {
                results = null;


                if (/*resultCode == Activity.RESULT_OK &&*/
                        mCapturedImageURI != null) {


                    //file:/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_202938_4605351355341258236.jpg

//                Uri[] results = new Uri[]{
//                        Uri.fromFile(new File("/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_200744_5778639102896116151.jpg"))
//                };
                    results = new Uri[]{
                            mCapturedImageURI
                    };
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
                    DLog.d("@www@" + Arrays.toString(results) + "@@" + mCapturedImageURI);

                }
            } else {
                if (intent != null) {
                    String dataString = intent.getDataString();
                    ClipData clipData = intent.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                } else {
                    //ooo
                    DLog.d("===================" + mCapturedImageURI);
                    results = new Uri[]{mCapturedImageURI};
                }
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }

        //[content://com.android.providers.media.documents/document/image%3A3060,
        // content://com.android.providers.media.documents/document/image%3A3061]

        DLog.d("@@" + Arrays.toString(results));
        mUploadMessages.onReceiveValue(results);
        mUploadMessages = null;
    }

    @SuppressLint("ObsoleteSdkInt")
    private void handleUploadMessage(final int requestCode, final int resultCode, final Intent data) {
        DLog.d("@@");
        Uri result = null;
        try {
            if (resultCode != RESULT_OK) {
                result = null;
            } else {
                // retrieve from the private variable if the intent is null
                result = data == null ? mCapturedImageURI : data.getData();
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;

        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            result = null;

            try {
                if (resultCode != RESULT_OK) {
                    result = null;
                } else {
                    // retrieve from the private variable if the intent is null
                    result = data == null ? mCapturedImageURI : data.getData();

                    //ooo
                    DLog.d("===========@@========" + mCapturedImageURI);
                }
            } catch (Exception e) {
                DLog.handleException(e);
                Toast.makeText(activity, "activity :" + e, Toast.LENGTH_LONG).show();
            }
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
            }
            mUploadMessage = null;
        }

    } // end of code for all versions except of Lollipop
}
