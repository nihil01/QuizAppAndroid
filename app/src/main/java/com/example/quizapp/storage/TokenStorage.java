package com.example.quizapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStorage {
    private SharedPreferences sharedPrf;
     private SharedPreferences.Editor prfEditor;
    public TokenStorage(Context ctx){
        sharedPrf = ctx.getSharedPreferences("JWTdata", Context.MODE_PRIVATE);
        prfEditor = sharedPrf.edit();
    }

    public boolean doesExist(){
        return !this.getToken().isEmpty();
    }

    public String getToken(){
        return sharedPrf.getString("token", "");
    }

    public void addToken(String token){
        prfEditor.putString("token", token);
        prfEditor.apply();
    }

    public void removeToken(){
        prfEditor.remove("token");
        prfEditor.commit();
    }

}
