package com.example.quizapp.services;

import com.example.quizapp.utils.network.*;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpAdapter {
    private static final String BASE_URL = "https://quiz.horosho.world/";

    public static RequestUser apiAdapterUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RequestUser.class);
    }

    public static RequestQuiz apiAdapterQuiz() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CustomHttpClient.getCustomClient())
                .build();

        return retrofit.create(RequestQuiz.class);
    }

    public static RequestBalance apiAdapterBalance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RequestBalance.class);
    }

    public static RequestFriends apiAdapterFriends(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RequestFriends.class);
    }

    public static RequestUserInfo apiAdapterUserInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RequestUserInfo.class);
    }

    public static RequestPasswordChange apiAdapterPasswordChange(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RequestPasswordChange.class);
    }

    public static RequestPurchase apiAdapterPurchase(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CustomHttpClient.getCustomClient())
                .build();
        return retrofit.create(RequestPurchase.class);
    }

}
