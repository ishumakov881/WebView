package com.filmhd1080.app;

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onesignal.OneSignal
import com.onesignal.OneSignal.initWithContext
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import io.appmetrica.analytics.push.AppMetricaPush

class TirAutoApp : Application() {




    override fun onCreate() {
        super.onCreate()



        if (ONE_SIGNAL.isNotEmpty()) {
            // OneSignal Initialization
//            OneSignal.startInit(this)
//                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                    //.setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
//                    .unsubscribeWhenNotificationsAreDisabled(true)
//                    .autoPromptLocation(true)
//                    .init();

            // OneSignal Initialization

            initWithContext(this, ONE_SIGNAL)

            // Enable verbose OneSignal logging to debug issues if needed.
            OneSignal.Debug.logLevel = com.onesignal.debug.LogLevel.VERBOSE




            //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
//            DLog.d("OneSignal Initialization");
//            OneSignal.startInit(this)
//                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                    //.setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
//                    //--.unsubscribeWhenNotificationsAreDisabled(true)
//                    //.autoPromptLocation(true)
//                    .init();

//            OneSignal.unsubscribeWhenNotificationsAreDisabled(false);
//            OSDeviceState device = OneSignal.getDeviceState();
//            if (device != null) {
//                String email = device.getEmailAddress();
//                String emailId = device.getEmailUserId();
//                String pushToken = device.getPushToken();
//                String userId = device.getUserId();
//
//                boolean enabled = device.areNotificationsEnabled();
//                boolean subscribed = device.isSubscribed();
//                boolean pushDisabled = device.isPushDisabled();
//
//                DLog.d("[" + enabled + "] " + subscribed + " " + pushDisabled);
//                DLog.d(String.valueOf(device.toJSONObject()));

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getApplicationContext(),
//                            new String[]{POST_NOTIFICATIONS}, 1);
//                }
//            }

            var id: String = OneSignal.User.onesignalId
            println("@@@@@@@@@@www" + id)
        }


        //io.appmetrica.analytics.ValidationException: Invalid ApiKey
        try {
            // Creating an extended library configuration.
            val config: AppMetricaConfig =
                AppMetricaConfig.newConfigBuilder(API_APPMETRICA_KEY).build()
            // Initializing the AppMetrica SDK.
            AppMetrica.activate(this, config)
            AppMetricaPush.activate(this)
//        AppMetricaPush.activate(
//                getApplicationContext(),
//                new FirebasePushServiceControllerProvider(this),
//                new HmsPushServiceControllerProvider(this)
//        );
        } catch (e: io.appmetrica.analytics.ValidationException) {
            //DLog.handleException(e);
        } catch (e: IllegalStateException) {
            println("$e")
        }
    }

    //https://docs.google.com/document/d/e/2PACX-1vT6GWga5ZDECsiTJZj0UGiys2l6vToEzSPH_bZd27WNlLCshV4JbkJanJkUERUTd3Iv7S6VL2NU3Nwe/pub

    //7c793af0-4784-4681-95fd-c07bb06cf15c
    //c651c4ff-d29e-4d9b-8601-a971c2e4711d
    companion object {
        private const val ONE_SIGNAL: String = "5b807064-3a4d-4001-b062-e672e7ffbb07"
        //ME ME ME @@ private const val ONE_SIGNAL: String = "b7ee58fd-ac54-4a6b-a28d-782a67de1672"

        private const val API_APPMETRICA_KEY: String = "652064c7-8614-4e27-9069-ced991faf345"

    }
}