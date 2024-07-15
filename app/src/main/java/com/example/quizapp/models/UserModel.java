package com.example.quizapp.models;

import java.util.ArrayList;
import java.util.HashMap;

public class UserModel {
    private String email, phone, password, authType;
    private ArrayList<HashMap> data = new ArrayList<>();
    private HashMap<String, String> result = new HashMap<>();

    public UserModel(){
        //
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setEmail(String email) {
        this.email = email;
        this.authType = "EMAIL";
    }

    public String getPhone() {
        return phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.authType = "PHONE";
    }

    public String getAuthType() {
        return authType;
    }

    public ArrayList<HashMap> getAuthEmail (){


        result.put("email", getEmail());
        result.put("password", getPassword());
        result.put("authType", getAuthType());

        data.add(result);
        return data;
    }

    public ArrayList<HashMap> getAuthPhone (){


        result.put("phone", getPhone());
        result.put("password", getPassword());
        result.put("authType", getAuthType());

        data.add(result);
        return data;
    }
}
