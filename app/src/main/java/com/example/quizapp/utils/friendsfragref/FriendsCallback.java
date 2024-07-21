package com.example.quizapp.utils.friendsfragref;

import com.example.quizapp.models.FriendsModel;

import java.util.List;

public interface FriendsCallback {
    void onFriendsLoaded (List<FriendsModel> data);
    void onError (String error);
}
