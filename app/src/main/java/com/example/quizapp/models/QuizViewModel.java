package com.example.quizapp.models;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class QuizViewModel extends ViewModel {
    private ArrayList<QuizModel> quizModelList;
    private String quizTopic;
    private String quizQuestionNum;

    public ArrayList<QuizModel> getQuizModelList(){
        return quizModelList;
    }

    public void setQuizModelList(ArrayList<QuizModel> quizModelList) {
        this.quizModelList = quizModelList;
    }

    public String getQuizQuestionNum() {
        return quizQuestionNum;
    }

    public void setQuizQuestionNum(String quizQuestionNum) {
        this.quizQuestionNum = quizQuestionNum;
    }

    public String getQuizTopic() {
        return quizTopic;
    }

    public void setQuizTopic(String quizTopic) {
        this.quizTopic = quizTopic;
    }
}
