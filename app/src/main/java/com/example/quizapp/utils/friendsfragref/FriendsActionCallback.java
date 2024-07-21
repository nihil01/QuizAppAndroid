package com.example.quizapp.utils.friendsfragref;

import com.example.quizapp.models.FriendsActionResultModel;

import java.util.List;

public interface FriendsActionCallback {
    void onActionCallback(String data);
    void onError(String err);
}
