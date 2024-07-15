package com.example.quizapp.frags;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.services.QuizService;
import com.example.quizapp.storage.TokenStorage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class StartQuizFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private ArrayList<HashMap<String, String>> requestQuizData = new ArrayList<>();

    Button startQuiz;

    public StartQuizFragment(){
        super(R.layout.fragment_start_quiz);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_start_quiz, container, false);

        //Greet user
        this.greetUser(view.findViewById(R.id.greeting_message));

        //Spinner
        Spinner spinnerTopic = view.findViewById(R.id.dropdown_menu_topic);
        Spinner spinnerQuestions = view.findViewById(R.id.dropdown_menu_questionum);

        String[] topics = { "Animals", "Brainteasers", "Entertainment", "For Kids",
                "History", "Geography", "Hobbies", "Newest", "Music" };

        Integer[] questions = { 5, 10, 15, 20 };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, topics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(adapter);
        spinnerTopic.setOnItemSelectedListener(this);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, questions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuestions.setAdapter(adapter2);
        spinnerQuestions.setOnItemSelectedListener(this);

        startQuiz = view.findViewById(R.id.buttonStartQuiz);

        startQuiz.setOnClickListener(v -> {
            if (requestQuizData.get(0).get("questionNum") != null && requestQuizData.get(0).get("topic") != null){
                TokenStorage storage;
                try{
                    storage = new TokenStorage(getActivity());
                    View dialogView = inflater.inflate(R.layout.fragment_loader, null);

                    AlertDialogCreator dialogCreator = new AlertDialogCreator(dialogView, getActivity(), "Loading quiz");
                    new QuizService(getActivity(), dialogCreator).sendQuizStartRequest(storage.getToken(), requestQuizData);
                    dialogCreator.setDialogWindow();

                }catch (RuntimeException exc){
                    Log.e("EXC_ERR", exc.getMessage());
                }

            }else{
                Log.d("DATA_QUIZ", requestQuizData.get(0).get("questionNum"));
            }

        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            HashMap <String, String> result;

            if (requestQuizData.isEmpty()){
                result = new HashMap<>();
                requestQuizData.add(result);
            }else{
                result = requestQuizData.get(0);
            }

            if (parent.getId() == R.id.dropdown_menu_questionum){
                result.put("questionNum", parent.getSelectedItem().toString());
            }else{
                result.put("topic", parent.getSelectedItem().toString());
            }
            requestQuizData.set(0, result);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "NOTHING SELECTED", Toast.LENGTH_SHORT).show();
    }

    public void greetUser(TextView v){
        int hour = LocalTime.now().getHour();

        if(hour >= 1 && hour < 5){
            v.setText("Good night!");
        }else if(hour >= 5 && hour < 13){
            v.setText("Good morning!");
        }else if(hour >= 13 && hour < 16){
            v.setText("Good afternoon!");
        }else if (hour >= 16 && hour < 21){
            v.setText("Good evening!");
        }else{
            v.setText("Good night!");
        }
    }

}
