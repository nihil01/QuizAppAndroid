package com.example.quizapp.services;

import android.util.Log;

import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.models.UserInfoModel;
import com.example.quizapp.utils.network.RequestUserInfo;
import com.example.quizapp.utils.profileuserinforef.ProfileUserInfoCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoService implements RequestUserInfo {

    ProfileUserInfoCallback cb;
    AlertDialogCreator alertDialogCreator;

    public UserInfoService(ProfileUserInfoCallback cb, AlertDialogCreator alertDialogCreator){
        this.cb = cb;
        this.alertDialogCreator = alertDialogCreator;
    }

    @Override
    public Call<UserInfoModel> requestUserInfo(String header) {
        Call<UserInfoModel> call = HttpAdapter.apiAdapterUserInfo().requestUserInfo(header);
        call.enqueue(new Callback<UserInfoModel>() {
            @Override
            public void onResponse(Call<UserInfoModel> call, Response<UserInfoModel> response) {
                try{
                    alertDialogCreator.unsetDialogWindow();
                    if (response.isSuccessful()){
                        cb.onUserInfo(response.body());
                    }else{
                        cb.onError("Could not load your profile!");
                    }
                }catch (RuntimeException exc){
                    alertDialogCreator.unsetDialogWindow();
                    Log.e("FAILURE", exc.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserInfoModel> call, Throwable throwable) {
                alertDialogCreator.unsetDialogWindow();
                Log.e("FAILURE", throwable.getMessage());
            }
        });
        return call;
    }
}
