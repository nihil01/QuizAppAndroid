package com.example.quizapp.frags;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.bottomNavigationEnum.Nav;
import com.example.quizapp.R;
import com.example.quizapp.services.UserService;
import com.example.quizapp.utils.activityref.FragmentDisplay;


public class RegisterFragment extends Fragment {
    public RegisterFragment(){
        super(R.layout.fragment_register);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ((FragmentDisplay)getActivity()).operateBottomNavBar(Nav.NAV_HIDE);

        Button loginBtn = view.findViewById(R.id.accountYes);

        // Set up the button listener inside the fragment

        loginBtn.setOnClickListener(v -> {
            if (getActivity() instanceof FragmentDisplay){
                ((FragmentDisplay)getActivity()).loadFragment(LoginFragment.class);
            }
        });

        Button regButton = view.findViewById(R.id.regBtn);
//        //action while pressing register button
        regButton.setOnClickListener(v -> {
            try {
                TextView emailPhoneField = view.findViewById(R.id.emailPhoneField);
                TextView passwordField = view.findViewById(R.id.passwordField);


                new UserService().processAuthInput(passwordField.getText().toString(), emailPhoneField.getText().toString(),
                        "REGISTRATION", getActivity());

            }catch (RuntimeException e){
                Log.e("runtime_exc_", e.getMessage());
            }

        });

        return view;
    }

}
