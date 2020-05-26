package com.example.shishengtao.fleamaerket.utils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtils {
    public static void sendOkHttpRequest(String address,String reqJson,okhttp3.Callback callback){

        //创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("reqJson",reqJson)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
