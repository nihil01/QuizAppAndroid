package com.example.quizapp.utils.network;

import com.example.quizapp.models.FriendsModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RequestFriends {
    @GET("/api/v1/friends")
    Call<List<HashMap<String, String>>> parametrizedF(@Header("Authorization") String header, @Query("state") String state, @Query("id") String id);
    @GET("/api/v1/friends")
    Call<List<FriendsModel>> parametrizedH (@Header("Authorization") String header, @Query("state") String state);
}
