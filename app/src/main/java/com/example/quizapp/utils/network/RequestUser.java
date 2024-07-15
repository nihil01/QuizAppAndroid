package com.example.quizapp.utils.network;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestUser {
    @POST("api/v1/registerUser")
    Call<ResponseBody> sendRegistrationRequest(@Body ArrayList<HashMap> userModelList);

    @POST("api/v1/loginUser")
    Call<ResponseBody> sendLoginRequest(@Body ArrayList<HashMap> userModelList);
}
