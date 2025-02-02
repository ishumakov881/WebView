package com.knopka.kz;

import android.app.Application;
import com.onesignal.OneSignal
import com.onesignal.OneSignal.initWithContext

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
//        try {
//            // Creating an extended library configuration.
//            AppMetricaConfig config = AppMetricaConfig.newConfigBuilder(API_APPMETRICA_KEY).build();
//            // Initializing the AppMetrica SDK.
//            AppMetrica.activate(this, config);
//
//            AppMetricaPush.activate(this);
////        AppMetricaPush.activate(
////                getApplicationContext(),
////                new FirebasePushServiceControllerProvider(this),
////                new HmsPushServiceControllerProvider(this)
////        );
//        }catch (io.appmetrica.analytics.ValidationException |java.lang.IllegalStateException e){
//            //DLog.handleException(e);
//        }
    }
    //c651c4ff-d29e-4d9b-8601-a971c2e4711d
    companion object {
        private const val OAI: kotlin.String = "fed3e169-a399-4d17-8f72-233701bc2db9"
    }
}