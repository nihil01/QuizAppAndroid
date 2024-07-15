package com.example.quizapp.services;

import android.content.Context;
import android.widget.Toast;

import com.example.quizapp.frags.RegisterFragment;
import com.example.quizapp.models.BalanceModel;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.activityref.FragmentDisplay;
import com.example.quizapp.utils.network.RequestBalance;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceService implements RequestBalance {
    private Context ctx;
    public int balance;

    public BalanceService(Context ctx){
        this.ctx = ctx;
    }
    @Override
    public Call<BalanceModel> sendHandleBalanceRequest(String header, ArrayList<HashMap<String, Integer>> balanceBody) {
        Call<BalanceModel> call = HttpAdapter.apiAdapterBalance().sendHandleBalanceRequest(header, balanceBody);

        call.enqueue(new Callback<BalanceModel>() {
            @Override
            public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                if (response.isSuccessful()){
                    balance = response.body().getBalance();
                    Toast.makeText(ctx, "Request successful!", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 401){
                    Toast.makeText(ctx, "Token has been expired. Log in again", Toast.LENGTH_SHORT).show();
                    new TokenStorage(ctx).removeToken();
                    ((FragmentDisplay) ctx).loadFragment(RegisterFragment.class, true);
                }else {
                    Toast.makeText(ctx, "Something went wrong. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BalanceModel> call, Throwable throwable) {
                Toast.makeText(ctx, "Could not get balance!", Toast.LENGTH_SHORT).show();
            }

        });

        return call;
    }
}
