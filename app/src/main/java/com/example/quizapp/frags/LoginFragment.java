package com.example.quizapp.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.services.UserService;
import com.example.quizapp.utils.activityref.FragmentDisplay;

public class LoginFragment extends Fragment {
    public LoginFragment(){
        super(R.layout.fragment_login);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);

        Button regBtn = view.findViewById(R.id.accountNo);

        // Set up the button listener inside the fragment

        regBtn.setOnClickListener(v -> {
            if (getActivity() instanceof FragmentDisplay){
                ((FragmentDisplay)getActivity()).loadFragment(RegisterFragment.class);
            }
        });

        Button logBtn = view.findViewById(R.id.logBtn);
//        //action while pressing login button
        logBtn.setOnClickListener(v -> {
            TextView emailPhoneField = view.findViewById(R.id.emailPhoneField);
            TextView passwordField = view.findViewById(R.id.passwordField);

            new UserService().processAuthInput(passwordField.getText().toString(), emailPhoneField.getText().toString(),
                    "LOGIN", getActivity());
        });

        return view;
    }
}
