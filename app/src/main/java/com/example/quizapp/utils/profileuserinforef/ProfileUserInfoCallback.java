package com.example.quizapp.utils.profileuserinforef;

import com.example.quizapp.models.UserInfoModel;

public interface ProfileUserInfoCallback {
    void onUserInfo(UserInfoModel model);
    void onError(String error);
}
