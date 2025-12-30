//package com.walhalla.webview.utility;
//
//import android.content.Context;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BankUtils {
//
//    public static List<Bank> readBanksFromJson(Context context) {
//        List<Bank> banks = new ArrayList<>();
//        try {
//            // Чтение JSON из файла в assets
//            InputStream inputStream = context.getAssets().open("banks.json");
//            int size = inputStream.available();
//            byte[] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            String json = new String(buffer, "UTF-8");
//
//            // Разбор JSON
//            JSONObject jsonObject = new JSONObject(json);
//            JSONArray jsonArray = jsonObject.getJSONArray("banks");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject bankObject = jsonArray.getJSONObject(i);
//                String id = bankObject.getString("id");
//                String engName = bankObject.getString("eng_name");
//                String ruName = bankObject.getString("ru_name");
//                String dist_url = bankObject.getString("dist_url");
//
//                banks.add(new Bank(id, engName, ruName, dist_url));
//            }
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//
//        return banks;
//    }
//}
