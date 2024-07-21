package com.example.quizapp;

import android.graphics.Insets;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizapp.bottomNavigationEnum.Nav;
import com.example.quizapp.frags.FriendsFragment;
import com.example.quizapp.frags.MainMenuFragment;
import com.example.quizapp.frags.ProfileFragment;
import com.example.quizapp.frags.RegisterFragment;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.activityref.FragmentDisplay;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements FragmentDisplay {
    private FragmentManager fragmentManager;

    public MainActivity(){
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).toPlatformInsets();
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        TokenStorage tokenStorage = new TokenStorage(this);

        if (savedInstanceState == null) {
            try {
                if (tokenStorage.doesExist()) {
                    Log.d("storage_data_", tokenStorage.getToken());
                    loadFragment(MainMenuFragment.class);
                } else {
                    loadFragment(RegisterFragment.class);
                }
            } catch (RuntimeException runtimeException) {
                Log.e("EXCEPTION_OCC", runtimeException.getMessage());
            }
        }
        NavigationBarView navBar = findViewById(R.id.navigation_bar_view);
        navBar.setSelectedItemId(R.id.home);

        navBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                loadFragment(MainMenuFragment.class);
                return true;
            }else if (item.getItemId() == R.id.profile){
                loadFragment(ProfileFragment.class);
                return true;
            }else if (item.getItemId() == R.id.friends){
                loadFragment(FriendsFragment.class);
                return true;
            }else{
                return false;
            }
        });
    }

    @Override
    public void loadFragment(Class cl) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, cl, null);
            transaction.commit();
    }

    @Override
    public void loadFragment(Fragment frag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, frag, null);
        transaction.commit();
    }

    @Override
    public void operateBottomNavBar(Nav state) {
        if (state == Nav.NAV_HIDE) {
            findViewById(R.id.navigation_bar_view).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.navigation_bar_view).setVisibility(View.VISIBLE);
        }
    }

}
