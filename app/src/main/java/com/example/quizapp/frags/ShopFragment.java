package com.example.quizapp.frags;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.services.PurchaseService;
import com.example.quizapp.storage.TokenStorage;

import java.util.regex.Pattern;

public class ShopFragment extends Fragment {

    private View view;
    private AlertDialog builder;

    public ShopFragment() {
        super(R.layout.fragment_shop);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.view = view;

        (view.findViewById(R.id.productCard)).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                generateDialogWindow(inflater);
                return true;
            }
            return false;
        });

        return view;
    }


    private void generateDialogWindow(LayoutInflater inflater){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        View v = inflater.inflate(R.layout.dialog_purchase, null);
        alertDialog.setView(v);
        alertDialog.setTitle("DIGITAL PURCHASE");

        AlertDialog dialog = alertDialog.create();

        dialog.show();
        try{
            Button btn = dialog.findViewById(R.id.btnDialogField);
            btn.setOnClickListener(l -> {
                this.processPurchase();
            });
        }catch (RuntimeException exc){
            Log.e("FAILURE", exc.getMessage());
        }

        this.builder = dialog;
    }

    private void processPurchase(){
        String phone = ((EditText)this.builder.findViewById(R.id.phoneDialogField)).getText().toString();
        String amount = ((EditText)this.builder.findViewById(R.id.amountField)).getText().toString();
        AlertDialogCreator alertDialogCreator =
                new AlertDialogCreator(getLayoutInflater().inflate(R.layout.fragment_loader, null), getContext(), "Processing a purchase..");
        PurchaseService purchaseService = new PurchaseService(getContext(), alertDialogCreator);
        TokenStorage tokenStorage = new TokenStorage(getContext());

        Pattern regexp = Pattern.compile("^(050|051|055| 070|077|010|099)\\d{7}");

        if (regexp.matcher(phone).matches()){
            this.builder.dismiss();
            alertDialogCreator.setDialogWindow();
            purchaseService.makeAPurchase(tokenStorage.getToken(), phone, "BALANCE_TOP_UP", amount);
                    }
        else {
            Toast.makeText(getContext(), "Invalid phone number. Try without +994", Toast.LENGTH_SHORT).show();
        }

    }

}
