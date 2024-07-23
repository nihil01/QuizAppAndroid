package com.example.quizapp.frags;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.models.UserInfoModel;
import com.example.quizapp.services.PasswordChangeService;
import com.example.quizapp.services.UserInfoService;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.activityref.FragmentDisplay;
import com.example.quizapp.utils.profilepasschangeref.ProfilePassChangeCallback;
import com.example.quizapp.utils.profileuserinforef.ProfileUserInfoCallback;

public class ProfileFragment extends Fragment {
    private View view;

    private Button changePass, logout;

    private TextView profileCredentials, profileBalance, profileRegDate;

    private EditText oldPass, newPass;

    public String oldPassword, newPassword;

    private UserInfoService userInfoService;
    private PasswordChangeService passwordChangeService;
    private TokenStorage tokenStorage;

    public ProfileFragment(){
        super(R.layout.fragment_profile);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        this.view = view;

        try{
            this.initialize(inflater);
        }catch (RuntimeException e){
            Log.e("FAILURE", e.getMessage());
        }


        return view;
    }

    public void initialize(LayoutInflater inflater){
        AlertDialogCreator alertDialogCreator = new AlertDialogCreator(inflater.inflate(R.layout.fragment_loader, null), getContext(), "Loading profile");

        this.userInfoService = new UserInfoService(new ProfileUserInfoCallback() {
            @Override
            public void onUserInfo(UserInfoModel model) {
                if (getActivity() != null){
                    getActivity().runOnUiThread(() -> loadUserInfo(model));
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null){
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        }, alertDialogCreator);

        this.tokenStorage = new TokenStorage(getContext());
        userInfoService.requestUserInfo(this.tokenStorage.getToken());
        alertDialogCreator.setDialogWindow();

        changePass = view.findViewById(R.id.passChange);
        changePass.setOnClickListener(l -> {
            oldPass = view.findViewById(R.id.oldPassField);
            newPass = view.findViewById(R.id.newPassField);

            if (oldPass.getText() != null && newPass.getText()!= null){
                if (newPass.length() > 8){
                    this.newPassword = newPass.getText().toString().trim();
                    this.oldPassword = oldPass.getText().toString().trim();
                    this.sendPassChangeForm(inflater);
                }else{
                    Toast.makeText(getContext(), "Password can't be less 8 symbols", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Password can't be empty", Toast.LENGTH_SHORT).show();
            }
        });

        logout = view.findViewById(R.id.accLogout);
        logout.setOnClickListener(a -> {
            tokenStorage.removeToken();
            ((FragmentDisplay) getContext()).loadFragment(RegisterFragment.class);
        });
    }

    public void loadUserInfo(UserInfoModel userInfoModel){
        profileCredentials = view.findViewById(R.id.profileCredentials);
        profileBalance = view.findViewById(R.id.profileBalance);
        profileRegDate = view.findViewById(R.id.profileRegDate);

        profileBalance.setText(userInfoModel.getBalance()+"\uD83D\uDCB2");
        profileRegDate.setText(userInfoModel.getRegistered());
        profileCredentials.setText(userInfoModel.getCredentials());
    }

    public void sendPassChangeForm(LayoutInflater inflater){
        AlertDialogCreator alertDialogCreator = new AlertDialogCreator(inflater.inflate(R.layout.fragment_loader, null), getContext(), "Changing password");
        this.passwordChangeService = new PasswordChangeService(new ProfilePassChangeCallback(){

            @Override
            public void onPassChange(String result) {
                if (getActivity() != null) {
                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                    tokenStorage.removeToken();
                    ((FragmentDisplay) getContext()).loadFragment(RegisterFragment.class);
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null){
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        }, alertDialogCreator);

        alertDialogCreator.setDialogWindow();
        this.passwordChangeService.sendPasswordChangeRequest(this.tokenStorage.getToken(),
                this.oldPassword, this.newPassword);

    }
}
