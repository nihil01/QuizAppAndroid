package com.example.quizapp.alertDialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.quizapp.R;

public class AlertDialogCreator {

    private View v;
    private Context ctx;
    private AlertDialog builder;
    private String message;

    public AlertDialogCreator(View v, Context ctx, String message){
        this.v = v;
        this.ctx = ctx;
        this.message = message;
        this.init();
    }

    private void init(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        builder.setCancelable(false);
        builder.setView(this.v);
        this.builder = builder.create();
        TextView txt = this.v.findViewById(R.id.textView5);
        txt.setText(message);
    }

    public void setDialogWindow(){
        if (!this.builder.isShowing()){
            this.builder.show();
        }
    }

    public void unsetDialogWindow(){
        if (this.builder.isShowing() && this.builder != null){
            this.builder.dismiss();
        }
    }

}
