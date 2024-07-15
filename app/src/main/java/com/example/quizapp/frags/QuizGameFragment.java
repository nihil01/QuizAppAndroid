package com.example.quizapp.frags;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.models.QuizModel;
import com.example.quizapp.services.BalanceService;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.activityref.FragmentDisplay;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizGameFragment extends Fragment {
    private View vi;

    private String quizTopic, quizQuestionAmount;
    private TextView quizHeader,
            quizQuestionsLeft,
            quizQuestion;
    private Button quizNext, quizCheckAnswer;

    private ArrayList<QuizModel> quizModel;
    private RadioGroup quizAnswers;
    private ImageView quizImage;

    private String currentCorrectAnswer, currentSelectedAnswer;
    private int currentQuestion, correct = 0, incorrect = 0;

    private int selectedAnswerButtonID;

    private boolean questionFlag = false;

    public QuizGameFragment(){
        super(R.layout.fragment_quiz_game);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_quiz_game, container, false);

        this.quizHeader = v.findViewById(R.id.quizHeader);
        this.quizImage = v.findViewById(R.id.quizImage);
        this.quizQuestion = v.findViewById(R.id.quizQuestion);
        this.quizQuestionsLeft = v.findViewById(R.id.quizQuestionLeft);
        this.quizAnswers = v.findViewById(R.id.quizRadioGroup);

        this.quizNext = v.findViewById(R.id.btnNextQuestion);
        this.quizCheckAnswer = v.findViewById(R.id.btnCheckAnswer);

        if (getArguments() != null){

            BalanceService balanceService = new BalanceService(getActivity());

            this.quizModel = (ArrayList<QuizModel>) getArguments().getSerializable("quizModelList");
            this.quizTopic = getArguments().getString("quizTopic");
            this.quizQuestionAmount = getArguments().getString("quizQuestionNum");

            this.initializeQuiz();
            this.quizNext.setEnabled(false);
            //check answer
            this.quizCheckAnswer.setOnClickListener(z -> {
                if (this.currentCorrectAnswer.equals(this.currentSelectedAnswer)){
                    this.questionFlag = true;
                    this.correct ++;
                    ((Button) v.findViewById(this.selectedAnswerButtonID)).setTextColor(Color.GREEN);
                    Toast.makeText(getActivity(), "CORRECT ANSWER!", Toast.LENGTH_SHORT).show();
                }else {
                    ((Button) v.findViewById(this.selectedAnswerButtonID)).setTextColor(Color.RED);

                    this.questionFlag = true;
                    for (int i = 0; i < this.quizAnswers.getChildCount(); i++) {
                        RadioButton rb = (RadioButton) this.quizAnswers.getChildAt(i);
                        if (rb.getText().toString().equals(this.currentCorrectAnswer)){
                            rb.setTextColor(Color.GREEN);
                        }
                    }
                    Toast.makeText(getActivity(), "INCORRECT ANSWER!", Toast.LENGTH_SHORT).show();
                    this.incorrect ++;
                }
                z.setEnabled(false);

                if ((this.currentQuestion == this.quizModel.size()) && this.questionFlag){
                    ArrayList<HashMap<String, Integer>> data = new ArrayList<>();
                    HashMap<String, Integer> hash = new HashMap<>();

                    TokenStorage storage = new TokenStorage(getActivity());
                    hash.put("balance", (this.correct - this.incorrect) * 100);
                    data.add(0, hash);
                    balanceService.sendHandleBalanceRequest(storage.getToken(), data);

                    new Handler().postDelayed(() -> this.quizNext.setEnabled(true), 1500);
                }else{
                    this.quizNext.setEnabled(true);
                }
            });

            this.quizNext.setOnClickListener(z -> {
                this.currentQuestion ++;
                if (this.currentQuestion <= this.quizModel.size()){
                    this.initializeQuiz();
                }else{
                    try{
                        ShowResultsFragment frag = new ShowResultsFragment();
                        Bundle bundle = new Bundle();

                        bundle.putInt("balance", balanceService.balance);
                        bundle.putInt("correct", this.correct);
                        bundle.putInt("incorrect", this.incorrect);
                        frag.setArguments(bundle);

                        ((FragmentDisplay)getActivity()).loadFragment(frag, true);
                    }catch (RuntimeException e){
                        Log.e("frag_err", e.getMessage());
                    }
                    z.setEnabled(false);
                }
            });

        }else{
            Log.e("QUIZ_STATUS", "NOT OK");
        }
        this.vi = v;
        return v;
    }

    //Main initializer
    private void initializeQuiz(){
        if (this.currentQuestion == 0){
            this.setQuizHeader();
        }else{
            this.updateHeader();
        }

            this.setCurrentCorrectAnswer(this.quizModel.get(this.currentQuestion - 1).getCorrect_answer());
            this.setQuizQuestion(this.quizModel.get(this.currentQuestion - 1).getQuestion());
            this.setQuizImage(this.quizModel.get(this.currentQuestion - 1).getImage_url());
            this.setQuizAnswers(this.quizModel.get(this.currentQuestion - 1).getAnswersList());

            this.quizCheckAnswer.setEnabled(false);

            this.currentSelectedAnswer = null;
            this.quizAnswers.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton btn = vi.findViewById(checkedId);
                if (btn != null){
                    selectedAnswerButtonID = btn.getId();
                    quizCheckAnswer.setEnabled(true);
                    currentSelectedAnswer = btn.getText().toString();
                }
            });

        if (this.currentQuestion == this.quizModel.size()){
            this.quizNext.setText("View Results");
            this.quizNext.setEnabled(false);
        }

    }

    private void setQuizImage(String imageUrl){
        if (imageUrl != "null"){
            this.quizImage.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUrl).into(this.quizImage);
        }
    }

    //PREPARE QUESTION
    private void setQuizQuestion(String question){
        this.quizQuestion.setText(question);
    }

    private void setCurrentCorrectAnswer(String answer){
        this.currentCorrectAnswer = answer;
    }

    //PREPARE ANSWERS
    private void setQuizAnswers(List<String> answers){
        this.quizAnswers.removeAllViews();
        for (int i = 0; i < answers.size(); i++) {
            RadioButton button = new RadioButton(getActivity());
            button.setLayoutParams(new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setText(answers.get(i));

            this.quizAnswers.addView(button);
        }
    }


    //HEADER MANIPULATION
    private void setQuizHeader(){
        this.currentQuestion = 1;
        this.quizHeader.setText("Topic: " + this.quizTopic);
        this.quizQuestionsLeft.setText("Question: "+this.currentQuestion+"/"+this.quizQuestionAmount);
    }

    private void updateHeader(){
        this.quizQuestionsLeft.setText("Question: "+this.currentQuestion+"/"+this.quizQuestionAmount);
    }

}
