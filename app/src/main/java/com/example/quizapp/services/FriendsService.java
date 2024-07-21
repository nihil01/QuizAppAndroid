package com.example.quizapp.services;

import android.util.Log;

import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.frags.FriendsFragment;
import com.example.quizapp.models.FriendsActionResultModel;
import com.example.quizapp.models.FriendsModel;
import com.example.quizapp.utils.friendsfragref.FriendsActionCallback;
import com.example.quizapp.utils.friendsfragref.FriendsCallback;
import com.example.quizapp.utils.network.RequestFriends;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsService implements RequestFriends {

    private AlertDialogCreator dialog;
    private FriendsCallback cb;
    private FriendsActionCallback cb2;


    public FriendsService(AlertDialogCreator alert, FriendsCallback cb){
        this.dialog = alert;
        this.cb = cb;
    }

    public FriendsService(AlertDialogCreator alert, FriendsActionCallback cb){
        this.dialog = alert;
        this.cb2 = cb;
    }


    @Override
    public Call<ResponseBody> parametrizedF(String header, String state, String id) {
        Call<ResponseBody> call = HttpAdapter.apiAdapterFriends().parametrizedF(header, state, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.unsetDialogWindow();
                try {
                    if (response.isSuccessful()) {
                        cb2.onActionCallback(response.body().string() + " " + response.code());
                    } else {
                        cb2.onError(response.errorBody().string() + " " + response.code());
                    }
                } catch (Exception e) {
                    Log.e("Error_exc_c " + response.code(), e.getMessage());
                    dialog.unsetDialogWindow();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                dialog.unsetDialogWindow();
                Log.d("FAILURE", throwable.getMessage());
            }
        });
        return call;
    }

    @Override
    public Call<List<FriendsModel>> parametrizedH(String header, String state) {
        Call<List<FriendsModel>> call = HttpAdapter.apiAdapterFriends().parametrizedH(header, state);
        call.enqueue(new Callback<List<FriendsModel>>() {
            @Override
            public void onResponse(Call<List<FriendsModel>> call, Response<List<FriendsModel>> response) {
                try{
                    dialog.unsetDialogWindow();
                    if (response.isSuccessful()){
                        if (response.body().get(0).getError() != null){
                            cb.onError(response.body().get(0).getError());
                        }else{
                            cb.onFriendsLoaded(response.body());
                        }
                    }else{
                        cb.onError("Something went wrong " + response.code());
                    }
                }catch (RuntimeException e){
                    Log.e("Error_exc_c", e.getMessage());
                    dialog.unsetDialogWindow();
                }
            }

            @Override
            public void onFailure(Call<List<FriendsModel>> call, Throwable throwable) {
                dialog.unsetDialogWindow();
                Log.d("FAILURE", throwable.getMessage());
            }
        });
        return call;
    }
}
