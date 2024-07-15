package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizapp.frags.MainMenuFragment;
import com.example.quizapp.frags.RegisterFragment;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.activityref.FragmentDisplay;

public class MainActivity extends AppCompatActivity implements FragmentDisplay {
    private FragmentManager fragmentManager;

    public MainActivity(){
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        TokenStorage tokenStorage = new TokenStorage(this);

        if (savedInstanceState == null) {
            try{
                if (tokenStorage.doesExist()) {
                    Log.d("storage_data_", tokenStorage.getToken());
                    loadFragment(MainMenuFragment.class, true);
                } else {
                    loadFragment(RegisterFragment.class, true);
                }
            }catch (RuntimeException runtimeException){
                Log.e("EXCEPTION_OCC", runtimeException.getMessage());
            }

        }
    }

    @Override
    public void loadFragment(Class cl, boolean addToStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, cl, null);

        if (addToStack) {
            transaction.addToBackStack(null);
        }
            transaction.commit();
    }

    @Override
    public void loadFragment(Fragment frag, boolean backStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, frag, null);

        if (backStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
