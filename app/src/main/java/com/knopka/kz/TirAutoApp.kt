package com.knopka.kz;

import android.app.Application
import com.onesignal.OneSignal
import com.onesignal.OneSignal.initWithContext
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

class TirAutoApp : Application() {

    //private static final String API_APPMETRICA_KEY = "6cb8d743-5912-42dc-a4fd-028e664df4e3";

    override fun onCreate() {
        super.onCreate()
        if (!android.text.TextUtils.isEmpty(OAI)) {
            // OneSignal Initialization
//            OneSignal.startInit(this)
//                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                    //.setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
//                    .unsubscribeWhenNotificationsAreDisabled(true)
//                    .autoPromptLocation(true)
//                    .init();

            // OneSignal Initialization

            initWithContext(this, OAI)
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

            var id: kotlin.String = OneSignal.User.onesignalId
            println("@@@@@@@@@@" + id)
        }


        //io.appmetrica.analytics.ValidationException: Invalid ApiKey
        try {
            // Creating an extended library configuration.
            val config: AppMetricaConfig =
                AppMetricaConfig.newConfigBuilder(API_APPMETRICA_KEY).build()
            // Initializing the AppMetrica SDK.
            AppMetrica.activate(this, config)
//            AppMetricaPush.activate(this)
//        AppMetricaPush.activate(
//                getApplicationContext(),
//                new FirebasePushServiceControllerProvider(this),
//                new HmsPushServiceControllerProvider(this)
//        );
        } catch (e: io.appmetrica.analytics.ValidationException) {
            //DLog.handleException(e);
        } catch (e: IllegalStateException) {
        }
    }
    //c651c4ff-d29e-4d9b-8601-a971c2e4711d
    companion object {
        private const val OAI: kotlin.String = "b7ee58fd-ac54-4a6b-a28d-782a67de1672"
        private const val API_APPMETRICA_KEY: kotlin.String = "be4e2c7d-f168-4445-b5b8-99802a02d168"

    }
}