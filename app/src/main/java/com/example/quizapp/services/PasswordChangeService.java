package com.example.quizapp.services;

import android.util.Log;

import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.utils.profilepasschangeref.ProfilePassChangeCallback;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChangeService{

    private ProfilePassChangeCallback cb;
    private AlertDialogCreator alertDialogCreator;

    public PasswordChangeService(ProfilePassChangeCallback cb, AlertDialogCreator alertDialogCreator){
        this.cb = cb;
        this.alertDialogCreator = alertDialogCreator;
    }

    public Call<ResponseBody> sendPasswordChangeRequest(String header,String oldPassword, String newPassword) {
        Call<ResponseBody> call = HttpAdapter.apiAdapterPasswordChange().sendPasswordChangeRequest(header, oldPassword, newPassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    alertDialogCreator.unsetDialogWindow();
                    if (response.isSuccessful()){
                        cb.onPassChange(response.body().string());
                        Log.e("FAILURE", response.body().string() + response.code());
                    }else{
                        cb.onError(response.errorBody().string());
                        Log.e("FAILURE", response.errorBody().string() + response.code());

                    }
                }catch (RuntimeException | IOException exc){
                    alertDialogCreator.unsetDialogWindow();
                    Log.e("FAILURE", exc.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                alertDialogCreator.unsetDialogWindow();
                Log.e("FAILURE", throwable.getMessage());
            }
        });
        return call;
    }
}
