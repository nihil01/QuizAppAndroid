package com.example.quizapp.utils.network;

import android.content.Context;

import com.example.quizapp.models.QuizModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RequestQuiz {
    @POST("api/v1/startGame")
    Call<List<QuizModel>> sendQuizStartRequest(@Header("Authorization") String header, @Body ArrayList<HashMap<String, String>> quizBody);
}
