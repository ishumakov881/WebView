//package com.walhalla.webview.utility;
//
//import static com.walhalla.webview.utility.ActivityUtils.TAG;
//import static com.walhalla.webview.utility.ActivityUtils.openBrowser;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.util.Log;
//import android.webkit.WebView;
//import android.widget.Toast;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class InAppBrowserUtils {
//    public static final String MARKET_CONSTANT = "market://details?id=";
//    private static final String PKG_NAME_VENDING = "com.android.vending";
//    public static final String GOOGLE_PLAY_CONSTANT = "https://play.google.com/store/apps/details?id=";
//
//
//    public static boolean isNspb(String url) {
//        return url != null && url.startsWith("intent://qr.nspk.ru");
//    }
//
//    public static boolean handleNspb(WebView webView, final String url0) {
//
//        String clearUrl = url0;
//
//        if (url0.startsWith("http://intent://qr.nspk.ru/")) {
//            clearUrl = clearUrl.replace("http://", "");
//            //url.startsWith("sberpay:")
//        }
//
//        //"pay.mironline.ru"
//        String NPSK_PACKAGENAME = "ru.nspk.sbpay";
//        String scheme = "https";
//
//        //=============================================================================
//
//        String[] parts = clearUrl.split("#");// Разбиваем строку на две части: до и после символа '#'
//        String intentPart = parts[0];
//
//        String intent_scheme = null;
//        if (parts.length > 1) {
//            String dataPart = parts[1];
//            // Получаем схему из строки intent
//            String[] schemeMatch = dataPart.split("scheme=");
//            if (schemeMatch.length > 1) {
//                intent_scheme = schemeMatch[1].split(";")[0];
//            }
//        }
//
//
//        //=============================================================================
//        //Log.d(TAG, intentPart);
//        //Log.d(TAG, dataPart);
//
//        Log.d(TAG, "{11111}" + Arrays.toString(parts));
//        //We use all url
//        String data = getDataFromIntentString(clearUrl);// Получаем данные из строки intent
//
//        // Формируем URL для Intent
//        String intentUrl = null;
//        if (scheme != null && data != null) {
//            intentUrl = scheme + "://" + data;
//        }
//
//        Log.d(TAG, "b: " + clearUrl);
//        Log.d(TAG, "b: " + intentUrl);
//
//        Uri uri = Uri.parse(clearUrl);
//
//        // Получение схемы и хоста
//        String nativeBankScheme = uri.getScheme();
//        String appHost = uri.getHost();
////        "id": "100000000007",
////                "eng_name": "Raiffeisenbank",
////                "ru_name": "Райффайзенбанк"
//        Log.d(TAG, "{nativeBankScheme} " + nativeBankScheme);
//        Log.d(TAG, "{appHost} " + appHost);//qr.nspk.ru
//
//
//        //==============================================================
//        if ("qr.nspk.ru".equals(appHost)) {
//            //ok
//        }
//
////        if(intent_scheme){
////
////        }
//
////     bank199999999999
////     <data scheme="bank999999999999" />
////     <data host="qr.nspk.ru" />
////     <data host="web.qr.nspk.ru" />
//
////        if(isPackageInstalled(webView, NPSK_PACKAGENAME)){
////
////        }else {
////            if (canLaunchUri(webView,
////                    //scheme + "://"
////                    //"https://qr.nspk.ru"
////                    intentUrl
////            )) {
////                //00000
////                defaultLaunch(webView, intentUrl);
////            }
////        }
//        if (canLaunchUri(webView.getContext(),
//                //scheme + "://"
//                //"https://qr.nspk.ru"
//                intentUrl
//        )) {
//            //00000
//            defaultLaunch(webView.getContext(), intentUrl);
//            //webView.loadUrl(intentUrl);
//        }
//
//        return true;
//
//    }
//
//    public static void sberpay(Activity context, String url) {
////        String[] parts = url.split("#");
////        String intentPart = parts[0];
////        String dataPart = #####;
//
//        // Получаем схему из строки intent
////        String scheme = null;
////        String[] schemeMatch = dataPart.split("scheme=");
////        if (schemeMatch.length > 1) {
////            scheme = schemeMatch[1].split(";")[0];
////        }
//
//
//        // Формируем URL для Intent
//        String intentUrl = url;
//        if (intentUrl.startsWith("http://sberpay:")) {
//            intentUrl = intentUrl.replace("http://", "");
//            //url.startsWith("sberpay:")
//        }
//
//        if (canLaunchUri(context, intentUrl)) {
//            defaultLaunch(context, intentUrl);
//        } else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Внимание")
//                    .setMessage("Программа SberPay не установленна")
//                    .setPositiveButton("Перейти на sberbank.com",
//                            (dialog, id) -> {
//                                dialog.cancel();
//                                openBrowser(context, "https://www.sberbank.com/ru/person/dist_services/sberbank-online-android");
//                            })
//                    .setNegativeButton("Закрыть",
//                            (dialog, id) -> {
//                                dialog.cancel();
//
//                            });
//            AlertDialog alert = builder
//                    .setCancelable(false).create();
//            alert.show();
//
//        }
//    }
//
//    public static boolean bank(WebView webView, String url) {
//        //В данном случае Intent;scheme=bank100000000007 указывает на то, что приложение схемы bank100000000007 должно обработать это намерение
//
//        //    https://qr.nspk.ru/BD10002VFK36IUK59TKPO95DDTMJHVL6?type=02&bank=@@@@@@@&sum=200000&cur=RUB&crc=5DFD#Intent;scheme=bankAlfa Bank;end
//        //    https://qr.nspk.ru/BD10002VFK36IUK59TKPO95DDTMJHVL6?type=02&bank=@@@@@@@&sum=200000&cur=RUB&crc=5DFD#Intent;scheme=bank100000000012;end
//        //    https://qr.nspk.ru/BD10002VFK36IUK59TKPO95DDTMJHVL6?type=02&bank=@@@@@@@&sum=200000&cur=RUB&crc=5DFD#Intent;scheme=bank100000000095;end
//
//
//        // {id банка который выбрали для оплаты}
//        // bank100000000111://qr.nspk.ru/BD10000B1J4IFAEN9D2O0Q2HRDAUAH8R?type=02&bank=@@@@@@@&sum=300000&cur=RUB&crc=BCF8#Intent;scheme=bank100000000007;end
//        // bankAlfa Bank://qr.nspk.ru/BD10000B1J4IFAEN9D2O0Q2HRDAUAH8R?type=02&bank=@@@@@@@&sum=300000&cur=RUB&crc=BCF8#Intent;scheme=bank100000000007;end
//
//        if (url.startsWith("http://bank")) {
//            url = url.replace("http://", "");
//        }
//        String[] parts = url.split("#");// Разбиваем строку на две части: до и после символа '#'
//        String intentPart = parts[0];
//
//        String dataScheme = null;
//        if (parts.length > 1) {
//            String dataPart = parts[1];
//            // Получаем схему из строки intent
//            String[] schemeMatch = dataPart.split("scheme=");
//            if (schemeMatch.length > 1) {
//                dataScheme = schemeMatch[1].split(";")[0];
//            }
//        }
//
//        Log.d(TAG, "{11111}" + Arrays.toString(parts));
//
//
//        String data = getDataFromIntentString(intentPart);// Получаем данные из строки intent
//
//        // Формируем URL для Intent
////        String intentUrl = null;
////        if (dataScheme != null && data != null) {
////            intentUrl = dataScheme + "://" + data;
////        }
////        intentUrl = data;
//
//        //==============================================================
//        //host
////        // Создание регулярного выражения для поиска домена
////        Pattern domainPattern = Pattern.compile("//(.*?)/");
////        Matcher matcher = domainPattern.matcher(intentPart);
////
////        // Поиск и извлечение домена
////        String host = null;
////        if (matcher.find()) {
////            host = matcher.group(1);
////        }
//
//        Uri uri = Uri.parse(url);
//
//        // Получение схемы и хоста
//        String nativeBankScheme = uri.getScheme();
//        String appHost = uri.getHost();
////        "id": "100000000007",
////                "eng_name": "Raiffeisenbank",
////                "ru_name": "Райффайзенбанк"
//        Log.d(TAG, "{nativeBankScheme} " + nativeBankScheme);
//        Log.d(TAG, "{appHost} " + appHost);//qr.nspk.ru
//        Log.d(TAG, "{dataScheme} " + dataScheme);//куда отправлять
//
//        //==============================================================
//        if ("qr.nspk.ru".equals(appHost)) {
////ok
//        }
//
//        // bank100000000111://qr.nspk.ru/B        WORK
//        // bank100000000111://                    NOTWORK
//
//        if (canLaunchUri(webView.getContext(), url)) {
//            openBrowser(webView.getContext(), url);
//        } else {
//            String newValue = data.replace(nativeBankScheme, dataScheme);
//            Log.d(TAG, "" + url);
//            Log.d(TAG, "" + newValue);
//
//            String m = url.replace(nativeBankScheme, "https");
//            openBrowser(webView.getContext(), m);//????
//            Log.d(TAG, "bank: " + m);
//            //webView.loadUrl(m);
//        }
//
//        //    https://qr.nspk.ru/BD10002VFK36IUK59TKPO95DDTMJHVL6?type=02&bank=@@@@@@@&sum=200000&cur=RUB&crc=5DFD#Intent;scheme=bankAlfa Bank;end
//        //    https://qr.nspk.ru/BD10002VFK36IUK59TKPO95DDTMJHVL6?type=02&bank=@@@@@@@&sum=200000&cur=RUB&crc=5DFD#Intent;scheme=bank100000000012;end
//        //    https://qr.nspk.ru/BD10002VFK36IUK59TKPO95DDTMJHVL6?type=02&bank=@@@@@@@&sum=200000&cur=RUB&crc=5DFD#Intent;scheme=bank100000000095;end
//
//
//        // {id банка который выбрали для оплаты}
//        // bank100000000111://qr.nspk.ru/BD10000B1J4IFAEN9D2O0Q2HRDAUAH8R?type=02&bank=@@@@@@@&sum=300000&cur=RUB&crc=BCF8#Intent;scheme=bank100000000007;end
//
//        // bankAlfa Bank://qr.nspk.ru/BD10000B1J4IFAEN9D2O0Q2HRDAUAH8R?type=02&bank=@@@@@@@&sum=300000&cur=RUB&crc=BCF8#Intent;scheme=bank{Raiffeisenbank};end
//        //bank{RosDorBank}://qr.nspk.ru/BD10000B1J4IFAEN9D2O0Q2HRDAUAH8R?type=02&bank={Promsvyazbank}&sum=300000&cur=RUB&crc=BCF8#Intent;scheme=bank{Raiffeisenbank};end
//        //bank{Bank VENETS}://qr.nspk.ru/BD10000B1J4IFAEN9D2O0Q2HRDAUAH8R?type=02&bank={Promsvyazbank}&sum=300000&cur=RUB&crc=BCF8#Intent;scheme=bank{Raiffeisenbank};end
//
//        //intent://qr.nspk.ru/BD10003JB9KBI44T9059VMQ49262NQUG?type=02&bank={Promsvyazbank}&sum=200000&cur=RUB&crc=2228#Intent;scheme=bank{AK BARS BANK};end
//        //intent://qr.nspk.ru/BD10003JB9KBI44T9059VMQ49262NQUG?type=02&bank=100000000010&sum=200000&cur=RUB&crc=2228#Intent;scheme=bank{KOSHELEV-BANK};end
//
//        return true;
//    }
//
//    public static void paymironlineru(Activity context, String url) {
//
//
//        if (url.startsWith("http://intent://pay.mironline.ru")) {
//            url = url.replace("http://", "");
//            //url.startsWith("sberpay:")
//        }
//
//        //"pay.mironline.ru"
//        String packageName = "ru.nspk.mirpay";
//        String scheme = "mirpay";
//
//
//        //=============================================================================
//
//        String[] parts = url.split("#");
//        String intentPart = parts[0];
//        if (parts.length > 1) {
//            String dataPart = parts[1];
//            // Получаем схему из строки intent
//            String[] schemeMatch = dataPart.split("scheme=");
//            if (schemeMatch.length > 1) {
//                scheme = schemeMatch[1].split(";")[0];
//            }
//        }
//
//        Log.d(TAG, Arrays.toString(parts));
//
//        String data = getDataFromIntentString(intentPart);// Получаем данные из строки intent
//
//        // Формируем URL для Intent
//        String intentUrl = null;
//        if (scheme != null && data != null) {
//            intentUrl = scheme + "://" + data;
//        }
//        //=============================================================================
//
//
////        String appUrl=clearUrl;
////        appUrl.replace()
//
//        if (canLaunchUri(context, scheme + "://")) {
//            Log.d(TAG, "@ " + url);
//            Log.d(TAG, "@ " + intentUrl);
//            defaultLaunch(context, intentUrl);
//        } else {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            String finalUrl = url;
//            builder.setTitle("Внимание")
//                    .setMessage("Программа MirPay не установленна")
////                    .setPositiveButton("Перейти на sberbank.com",
////                            (dialog, id) -> {
////                                dialog.cancel();
////                                openBrowser(context, "https://www.sberbank.com/ru/person/dist_services/sberbank-online-android");
////                            })
//
//                    .setPositiveButton("Перейти на Google Play",
//                            (dialog, id) -> {
//                                dialog.cancel();
//                                openMarketApp(context, packageName);
//                            })
//
//                    .setNeutralButton("Перейти на сайт",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    String clearUrl = finalUrl;
//                                    if (clearUrl.startsWith("intent://pay.mironline.ru")) {
//                                        clearUrl = clearUrl.replace("intent://", "https://");
//                                    }
//                                    defaultLaunch(context, clearUrl);
//                                }
//                            })
//
//
//                    .setNegativeButton("Закрыть",
//                            (dialog, id) -> {
//                                dialog.cancel();
//
//                            });
//            AlertDialog alert = builder
//                    .setCancelable(false).create();
//            alert.show();
//        }
//
//        //=============================================================================
//    }
//
//    public static void openMarketApp(Context context, final String packageName) {
//        try {
//            Uri uri = Uri.parse(MARKET_CONSTANT + packageName);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.setPackage(PKG_NAME_VENDING);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.getApplicationContext().startActivity(intent);
//        } catch (android.content.ActivityNotFoundException anfe) {
//            try {
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
//            } catch (ActivityNotFoundException a) {
//                openBrowser(context, GOOGLE_PLAY_CONSTANT + packageName);
//            }
//        }
//    }
//
//    private static void defaultLaunch(Context context, String clearUrl) {
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clearUrl));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            Toast.makeText(context, "Приложение не найдено...", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private static boolean canLaunchUri(Context context, String uri) {
//        final PackageManager pm = context.getPackageManager();
//        final Intent intent = new Intent(Intent.ACTION_VIEW);
////        if (dataType != null) {
////            intent.setDataAndType(Uri.parse(uri), dataType);
////        } else {
//        intent.setData(Uri.parse(uri));
////        }
//
//        List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);
//        if (resInfos.size() > 0) {
//            Log.d(TAG, "Found Activities that handle uri: " + uri);
//
////            boolean shouldGetAppList = false;
////            try {
////                shouldGetAppList = options.has("getAppList") && options.getBoolean("getAppList") == true;
////            } catch (JSONException e) {}
//
////            if (shouldGetAppList) {
////                JSONObject obj = new JSONObject();
////                JSONArray appList = new JSONArray();
////
////                for(ResolveInfo resolveInfo : resInfos) {
////                    try {
////                        appList.put(resolveInfo.activityInfo.packageName);
////                    } catch (Exception e) {
////                        //Do Nothing
////                    }
////                }
////
////                try {
////                    obj.put("appList", wrap(appList));
////                } catch(Exception e) {
////
////                }
////                callbackContext.success(obj);
////            } else {
////                callbackContext.success();
////            }
//            return true;
//        } else {
//            Log.d(TAG, "No Activities found that handle uri: " + uri);
//            //callbackContext.error("No application found.");
//        }
//        return false;
//    }
//
//    public static boolean isPackageInstalled(Context context, String packageName) {
//        boolean found = true;
//        PackageManager manager = context.getPackageManager();
//        try {
//            manager.getPackageInfo(packageName,//PackageManager.GET_ACTIVITIES
//                    PackageManager.GET_SIGNATURES
//            );
//        } catch (PackageManager.NameNotFoundException e) {
//            found = false;
//        }
//        return found;
//    }
//
//    //Запрос может быть с intent:// и без него
//    private static String getDataFromIntentString(String intentPart) {
//        String[] dataMatch = intentPart.split("intent://");
//        if (dataMatch.length > 1) {
//            return dataMatch[1];
//        }
//        return intentPart;
//    }
//}
