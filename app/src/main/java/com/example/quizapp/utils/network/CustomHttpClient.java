package com.example.quizapp.utils.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class CustomHttpClient {
    public static OkHttpClient getCustomClient(){
        return new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
