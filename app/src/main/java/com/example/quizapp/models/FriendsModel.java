package com.example.quizapp.models;

import java.io.Serializable;

public class FriendsModel {
    private String credentials;
    private String balance;
    private String status;
    private String friendship_id;

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public String getFriendship_id() {
        return friendship_id;
    }

    public void setFriendship_id(String friendship_id) {
        this.friendship_id = friendship_id;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
