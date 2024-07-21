package com.example.quizapp.utils.network;

import com.example.quizapp.models.UserInfoModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RequestUserInfo {
    @GET("api/v1/userInfo")
    Call<UserInfoModel> requestUserInfo(@Header("Authorization") String header);
}
