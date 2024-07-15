package com.example.quizapp.frags;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.models.FriendsModel;
import com.example.quizapp.services.FriendsService;
import com.example.quizapp.storage.TokenStorage;

import java.util.List;

public class FriendsFragment extends Fragment {
    public FriendsFragment(){
        super(R.layout.fragment_friends);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        try {
            View dialogView = inflater.inflate(R.layout.fragment_loader, null);
            AlertDialogCreator dialogCreator = new AlertDialogCreator(dialogView, getActivity(), "Loading friends");
            TokenStorage tokenStorage = new TokenStorage(getActivity());
            FriendsService friendsService = new FriendsService(getActivity(), dialogCreator);

            friendsService.parametrizedH(tokenStorage.getToken(), "getFriends");
            dialogCreator.setDialogWindow();

        } catch (Exception e) {
            Log.e("ERROR_OCC", e.getMessage());
        }
        return view;
    }

    public static void initializeUI(List<FriendsModel> friendsData){

    }


}
