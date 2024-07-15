package com.example.quizapp.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.frags.FriendsFragment;
import com.example.quizapp.models.FriendsModel;
import com.example.quizapp.utils.network.RequestFriends;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsService implements RequestFriends {

    private Context ctx;
    private AlertDialogCreator dialog;

    public FriendsService(Context ctx, AlertDialogCreator alert){
        this.ctx = ctx;
        this.dialog = alert;
    }


    @Override
    public Call<List<HashMap<String, String>>> parametrizedF(String header, String state, String id) {
        return null;
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
                        FriendsFragment.initializeUI(response.body());
                    }else{
                        Toast.makeText(ctx, "Something went wrong. " + response.code(), Toast.LENGTH_LONG).show();
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
        return null;    }
}
