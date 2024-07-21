package com.example.quizapp.utils.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RequestPurchase {
    @POST("api/v1/purchase")
    @FormUrlEncoded
    Call<ResponseBody> makeAPurchase(
            @Header("Authorization") String header,
            @Field("data") String data,
            @Field("productType") String productType,
            @Field("productAmount") String amount
    );
}
