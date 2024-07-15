package com.example.quizapp.utils.network;

import com.example.quizapp.models.BalanceModel;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RequestBalance {
    @POST("/api/v1/handleBalance")
    Call<BalanceModel> sendHandleBalanceRequest(@Header("Authorization") String header, @Body ArrayList<HashMap<String, Integer>> balanceBody);
}
