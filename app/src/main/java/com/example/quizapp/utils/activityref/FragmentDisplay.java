package com.example.quizapp.utils.activityref;

import androidx.fragment.app.Fragment;

import com.example.quizapp.bottomNavigationEnum.Nav;

public interface FragmentDisplay {
    void loadFragment(Class<? extends Fragment> frag);
    void loadFragment(Fragment frag);
    void operateBottomNavBar(Nav state);
}
