package com.example.quizapp.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.frags.MainMenuFragment;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.activityref.FragmentDisplay;
import com.example.quizapp.utils.network.RequestUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserService implements RequestUser {
    private Context ctx;

    public boolean processAuthInput(String password, String emailPhone, String authType, Context ctx){

        Pattern emailChecker = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Pattern phoneChecker = Pattern.compile("^(10|50|51|55|70|77|99)\\d{7}");

        this.ctx = ctx;

        if (password.isEmpty() || password.length() < 8){
            Toast.makeText(ctx, "Password must contain at least 8 characters! Try again.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (emailChecker.matcher(emailPhone).matches() &&
                !phoneChecker.matcher(emailPhone).matches()){

            UserModel userModel = new UserModel();
            userModel.setEmail(emailPhone);
            userModel.setPassword(password);

            if (authType.equals("REGISTRATION")) {
                sendRegistrationRequest(userModel.getAuthEmail());
            } else {
                sendLoginRequest(userModel.getAuthEmail());
            }

            return true;
        }else if (!emailChecker.matcher(emailPhone).matches() &&
                phoneChecker.matcher(emailPhone).matches()){
            UserModel userModel = new UserModel();
            userModel.setEmail(emailPhone);
            userModel.setPassword(password);

            if (authType.equals("REGISTRATION")) {
                sendRegistrationRequest(userModel.getAuthPhone());
            } else {
                sendLoginRequest(userModel.getAuthPhone());
            }
            return true;
        }else{
            Toast.makeText(ctx, "Invalid email or phone number! Try again.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public Call<ResponseBody> sendRegistrationRequest(ArrayList<HashMap> userModelList) {

        Call<ResponseBody> call = HttpAdapter.apiAdapterUser().sendRegistrationRequest(userModelList);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()){
                        Toast.makeText(ctx, "Registration Successful !", Toast.LENGTH_SHORT).show();
                        TokenStorage storage = new TokenStorage(ctx);
                        Headers headers = response.headers();
                        String data = headers.get("Authorization");
                        if (data != null) storage.addToken(data);
                        ((FragmentDisplay) ctx).loadFragment(MainMenuFragment.class, true);
                    } else if (response.code() == 429){
                    Toast.makeText(ctx, "Too many requests! Wait a bit",
                            Toast.LENGTH_SHORT).show();
                }else{
                        Toast.makeText(ctx, "Registration wasn't successful. Mind to change credentials",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (RuntimeException e) {
                    Toast.makeText(ctx, "ERROR WHILE READING BODY", Toast.LENGTH_SHORT).show();
                    Log.e("network_error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(ctx, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        return call;
    }

    @Override
    public Call<ResponseBody> sendLoginRequest(ArrayList<HashMap> userModelList) {


        Call<ResponseBody> call = HttpAdapter.apiAdapterUser().sendLoginRequest(userModelList);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()){
                        Toast.makeText(ctx, "Login successful!", Toast.LENGTH_SHORT).show();
                        TokenStorage storage = new TokenStorage(ctx);
                        Headers headers = response.headers();
                        String data = headers.get("Authorization");
                        if (data != null) storage.addToken(data);
                        ((FragmentDisplay) ctx).loadFragment(MainMenuFragment.class, true);
                    }else if (response.code() == 429){
                        Toast.makeText(ctx, "Too many requests! Wait a bit",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ctx, "Login wasn't successful. Mind to change credentials",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (RuntimeException e) {
                    Log.e("network_error", e.getMessage());
                    Toast.makeText(ctx, "ERROR WHILE READING BODY", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(ctx, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        return call;
    }
}
