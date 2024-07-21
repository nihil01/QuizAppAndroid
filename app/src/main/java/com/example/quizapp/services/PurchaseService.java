package com.example.quizapp.services;

import android.content.Context;
import android.widget.Toast;

import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.utils.network.RequestPurchase;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseService implements RequestPurchase {
    private Context ctx;
    private AlertDialogCreator dialog;

    public PurchaseService(Context ctx, AlertDialogCreator alertDialogCreator){
        this.ctx = ctx;
        this.dialog = alertDialogCreator;
    }

    @Override
    public Call<ResponseBody> makeAPurchase(String header, String data, String productType, String productAmount) {
        Call<ResponseBody> call = HttpAdapter.apiAdapterPurchase().makeAPurchase(header, data, productType, productAmount);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.unsetDialogWindow();
                    if (response.isSuccessful()){
                        Toast.makeText(ctx, response.body().string() + " " + response.code(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ctx, response.errorBody().string() + " " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    dialog.unsetDialogWindow();
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                dialog.unsetDialogWindow();
                Toast.makeText(ctx, "Response not successful (FAILURE) ", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}
