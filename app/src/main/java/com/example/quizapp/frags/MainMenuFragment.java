package com.example.quizapp.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.utils.activityref.FragmentDisplay;


public class MainMenuFragment extends Fragment {

    public MainMenuFragment(){
        super(R.layout.fragment_main_menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);


        Button startQuiz = view.findViewById(R.id.buttonQuizzesMenu);
        Button profileMenu = view.findViewById(R.id.buttonProfileMenu);
        Button friendMenu = view.findViewById(R.id.buttonFriendsMenu);
        Button shop = view.findViewById(R.id.btnShop);

        startQuiz.setOnClickListener(v -> {
            ((FragmentDisplay) getActivity()).loadFragment(StartQuizFragment.class, true);
        });

        profileMenu.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "In development!", Toast.LENGTH_SHORT).show();
        });


        friendMenu.setOnClickListener(v -> {
            ((FragmentDisplay)getActivity()).loadFragment(FriendsFragment.class, true);
        });

        shop.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "In development!", Toast.LENGTH_LONG).show();
        });

        return view;
    }
}
