package com.example.quizapp.utils.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RequestPasswordChange {
    @POST("api/v1/changePass")
    @FormUrlEncoded
    Call<ResponseBody> sendPasswordChangeRequest(@Header("Authorization") String header, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);
}
