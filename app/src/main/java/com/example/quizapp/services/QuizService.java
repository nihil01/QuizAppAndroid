package com.example.quizapp.services;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.frags.QuizGameFragment;
import com.example.quizapp.frags.RegisterFragment;
import com.example.quizapp.models.QuizModel;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.activityref.FragmentDisplay;
import com.example.quizapp.utils.network.RequestQuiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizService implements RequestQuiz{
    private Context ctx;
    private AlertDialogCreator dialog;

    public QuizService(Context ctx, AlertDialogCreator dialog){
        this.ctx = ctx;
        this.dialog = dialog;
    }

    @Override
    public Call<List<QuizModel>> sendQuizStartRequest(String token, ArrayList<HashMap<String, String>> quizBody) {

        Call<List<QuizModel>> call = HttpAdapter.apiAdapterQuiz().sendQuizStartRequest(token, quizBody);

        call.enqueue(new Callback<List<QuizModel>>() {
            @Override
            public void onResponse(Call<List<QuizModel>> call, Response<List<QuizModel>> response) {
                try{
                    dialog.unsetDialogWindow();
                    if (response.isSuccessful()){

                        Bundle newBundle = new Bundle();

                        ArrayList<QuizModel> quizList = new ArrayList<>(response.body());

                        newBundle.putSerializable("quizModelList", quizList);

                        newBundle.putString("quizTopic", quizBody.get(0).get("topic"));
                        newBundle.putString("quizQuestionNum", quizBody.get(0).get("questionNum"));

                        QuizGameFragment frag = new QuizGameFragment();
                        frag.setArguments(newBundle);

                        ((FragmentDisplay) ctx).loadFragment(frag);
                    }else if (response.code() == 401 || response.code() == 500){
                        Toast.makeText(ctx, "Token has been expired. Log in again", Toast.LENGTH_LONG).show();
                        new TokenStorage(ctx).removeToken();
                        ((FragmentDisplay) ctx).loadFragment(RegisterFragment.class);
                    }else{
                        Toast.makeText(ctx, "Something went wrong. Try again later. Code " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }catch (RuntimeException e){
                    Log.e("Error_exc_c", e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<QuizModel>> call, Throwable throwable) {
                dialog.unsetDialogWindow();
                Log.d("FAILURE", throwable.getMessage());
            }
        });
        return null;
    }

}
