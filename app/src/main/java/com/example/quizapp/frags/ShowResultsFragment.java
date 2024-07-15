package com.example.quizapp.frags;

import android.net.Uri;
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

import com.example.quizapp.R;
import com.example.quizapp.utils.activityref.FragmentDisplay;
import com.example.quizapp.utils.playSound.soundPlayer;

public class ShowResultsFragment extends Fragment {
    private View v;
    private int balance, correct, incorrect;

    public ShowResultsFragment(){
        super(R.layout.fragment_show_results);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_show_results, container, false);

        this.v = view;

        this.setArgumentsUI(getArguments());

        return view;
    }


    private void setArgumentsUI(Bundle bdl){
        try{
            this.balance = Integer.parseInt(bdl.get("balance").toString());
            this.correct = Integer.parseInt(bdl.get("correct").toString());
            this.incorrect = Integer.parseInt(bdl.get("incorrect").toString());

            Log.d("balance_", String.valueOf(this.balance));
            Log.d("correct_", String.valueOf(this.correct));
            Log.d("incorrect_", String.valueOf(this.incorrect));

            TextView txt = this.v.findViewById(R.id.quizResultInfo);
            txt.setText("You finished the Quiz ! You answered " + this.correct + "/" +
                    (this.correct+this.incorrect) +". Your balance has been updated.");

            TextView balance = this.v.findViewById(R.id.showBalance);
            balance.setText("Current balance is: " + (this.balance));

            Button btn = this.v.findViewById(R.id.btnMainScreen);
            try{
                soundPlayer.play(getActivity());
            }catch (Exception e){
                Log.e("sound_exc", e.getMessage());
            }

            btn.setOnClickListener(v -> {
                ((FragmentDisplay)getActivity()).loadFragment(MainMenuFragment.class, true);
            });
        }catch (RuntimeException e){
            Log.e("runtime_err", e.getMessage());
        }
    }
}
