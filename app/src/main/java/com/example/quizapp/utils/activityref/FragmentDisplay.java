package com.example.quizapp.utils.activityref;

import androidx.fragment.app.Fragment;

public interface FragmentDisplay {
    void loadFragment(Class<? extends Fragment> frag, boolean backStack);
    void loadFragment(Fragment frag, boolean backStack);
}
