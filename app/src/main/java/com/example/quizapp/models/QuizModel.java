package com.example.quizapp.models;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizModel implements Serializable {
    private String question, image_url, correct_answer, answers;
    private List<String> answersList;


    public String getAnswers() {
        return answers;
    }

    public void setAnswersList(List<String> answersList) {
        this.answersList = answersList;
    }


    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswersList() {
        if (answersList == null) {
            String[] answersArray = answers.replace("[", "").replace("]", "").replace("'", "").split(", ");
            answersList = new ArrayList<>(Arrays.asList(answersArray));
        }
        return answersList;
    }
}
