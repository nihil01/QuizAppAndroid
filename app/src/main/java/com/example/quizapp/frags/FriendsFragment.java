package com.example.quizapp.frags;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.alertDialog.AlertDialogCreator;
import com.example.quizapp.models.FriendsActionResultModel;
import com.example.quizapp.models.FriendsModel;
import com.example.quizapp.services.FriendsService;
import com.example.quizapp.storage.TokenStorage;
import com.example.quizapp.utils.friendsfragref.FriendsActionCallback;
import com.example.quizapp.utils.friendsfragref.FriendsCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

enum DataList{ NO_FRIENDS, FRIENDS }

public class FriendsFragment extends Fragment {

    private View v;

    private LinearLayout layoutFriends,friendsAwaitingLayout,
            btnListFriendsAwaiting,btnListFriends;

    private FriendsService friendsService;
    private AlertDialogCreator alertDialogCreator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        v = view;

        layoutFriends = v.findViewById(R.id.friendsLayout);
        friendsAwaitingLayout = v.findViewById(R.id.friendsAwaitingLayout);
        btnListFriends = v.findViewById(R.id.btnAreaFriends);
        btnListFriendsAwaiting = v.findViewById(R.id.btnAreaFriendsAwaiting);

        EditText txtField = v.findViewById(R.id.friendsSearchField);
        Button searchBtn = v.findViewById(R.id.friendsSearchBtn);

        try {
            loadFriends(inflater.inflate(R.layout.fragment_loader, null));

            txtField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 7){
                        searchBtn.setEnabled(true);
                    }else{
                        searchBtn.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //
                }
            });

            searchBtn.setOnClickListener(btn -> {
                String txt = txtField.getText().toString().substring(1).toUpperCase();
                this.loadAction("add", txt);
            });

        }catch (RuntimeException exc){
            Log.e("FAILURE", Log.getStackTraceString(exc));
        }
        return v;
    }

    private void loadAction(String state, String id){
        TokenStorage tokenStorage = new TokenStorage(getActivity());
        this.alertDialogCreator.setDialogWindow();

        this.friendsService = new FriendsService(alertDialogCreator, new FriendsActionCallback() {
            @Override
            public void onActionCallback(String data) {
                Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String err) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("salam", "token: " + tokenStorage.getToken()+  " state: " + state + " id: " + id);

        this.friendsService.parametrizedF(tokenStorage.getToken(), state, id);
    }


    private void loadFriends(View dialogView) {
        TokenStorage tokenStorage = new TokenStorage(getContext());
        this.alertDialogCreator = new AlertDialogCreator(dialogView, getContext(), "Loading friends");

        this.friendsService = new FriendsService(this.alertDialogCreator, new FriendsCallback() {
            @Override
            public void onFriendsLoaded(List<FriendsModel> data) {
                if (getActivity() != null){
                    getActivity().runOnUiThread(() -> initializeUserLists(data));
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null){
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        this.alertDialogCreator.setDialogWindow();

        this.friendsService.parametrizedH(tokenStorage.getToken(), "getFriends");
    }


    private void initializeUserLists(List<FriendsModel> friendsData) {
        try {
            List<FriendsModel> friends = new ArrayList<>();
            List<FriendsModel> friendsAwaiting = new ArrayList<>();

            int friends_pag = 0, friends_awaiting_pag = 0;

            for (int i = 0; i < friendsData.size(); i++) {
                if (friendsData.get(i).getStatus().equals("friends")) {
                    friends.add(friendsData.get(i));
                    friends_pag++;
                } else {
                    friendsAwaiting.add(friendsData.get(i));
                    friends_awaiting_pag++;
                }
            }
            Log.d("data_to_count", friends_pag + " " + friends_awaiting_pag);

            friends_pag %= 5;
            friends_awaiting_pag %= 5;

            Log.d("data_to_count", friends_pag + "");
            Log.d("data_to_count", friends_awaiting_pag + "");

            int pagStart = 0;
            for (int i = 1; i <= friends_pag; i++) {
                Button btn = new Button(getContext());
                btn.setText(String.valueOf(i));

                int finalPagStart = pagStart;

                btn.setOnClickListener(k -> {
                    drawUIElements(finalPagStart, finalPagStart + 5, friends, DataList.FRIENDS, layoutFriends);
                });
                pagStart += 5;
                btnListFriends.addView(btn);
            }

            pagStart = 0;
            for (int i = 1; i <= friends_awaiting_pag; i++) {
                Button btn = new Button(getActivity());
                btn.setText(String.valueOf(i));

                int finalPagStart = pagStart;

                btn.setOnClickListener(k -> {
                    drawUIElements(finalPagStart, finalPagStart + 5, friendsAwaiting, DataList.NO_FRIENDS, friendsAwaitingLayout);
                });
                pagStart += 5;
                btnListFriendsAwaiting.addView(btn);
            }

            drawUIElements(0, friends.size(), friends, DataList.FRIENDS, layoutFriends);
            drawUIElements(0, friendsAwaiting.size(), friendsAwaiting, DataList.NO_FRIENDS, friendsAwaitingLayout);

        } catch (Exception e) {
            Log.e("FriendsFragment", "Error: " + Log.getStackTraceString(e));;
        }
    }

    private void drawUIElements(int start, int end, List<FriendsModel> data,
                                       DataList dataList, LinearLayout layout) {
        layout.removeAllViews();

        if (data.isEmpty()){
            LinearLayout innerLayout = new LinearLayout(v.getContext());

            innerLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );


            TextView txt = new TextView(getContext());
            txt.setText("No friends are awaiting for your action.");
            params.topMargin = 56;

            innerLayout.setLayoutParams(params);
            innerLayout.addView(txt);
            layout.addView(innerLayout);
        }

        for (int i = start; i < end && i < data.size(); i++) {
            if (i >= end) break;
            try {
                LinearLayout innerLayout = new LinearLayout(v.getContext());
                innerLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView txt = new TextView(getContext());
                Button btn = new Button(getContext());

                if (dataList == DataList.NO_FRIENDS) {
                    Button btn2 = new Button(v.getContext());
                    btn.setText("✅");
                    btn2.setText("❎");

                    AtomicBoolean ticked = new AtomicBoolean(false);
                    AtomicBoolean ticked1 = new AtomicBoolean(false);

                    int finalI = i;
                    btn.setOnClickListener(k -> {
                        if (!ticked.get()){
                            Toast.makeText(getActivity(), "Are you sure? Press one more time to accept.", Toast.LENGTH_SHORT).show();
                            ticked.set(true);
                        }else{
                            try{
                                k.setEnabled(false);
                                this.loadAction("accept", data.get(finalI).getFriendship_id());
                            }catch (RuntimeException e){
                                Log.e("FAILURE", e.getMessage());
                            }
                        }
                    });

                    btn2.setOnClickListener(k -> {
                        if (!ticked1.get()){
                            Toast.makeText(getActivity(), "Are you sure? Press one more time to reject.", Toast.LENGTH_SHORT).show();
                            ticked1.set(true);
                        }else{
                            try{
                                k.setEnabled(false);
                                this.loadAction("reject", data.get(finalI).getFriendship_id());
                            }catch (RuntimeException e){
                                Log.e("FAILURE", e.getMessage());
                            }
                        }
                    });

                    txt.setText(String.format(" (#%s) ", data.get(i).getFriendship_id()) + data.get(i).getCredentials());
                    innerLayout.addView(txt);
                    innerLayout.addView(btn);

                    innerLayout.addView(btn2);
                } else {
                    btn.setText("Remove friend");
                    txt.setText(String.format(" (#%s) ", data.get(i).getFriendship_id()) + " " +
                            data.get(i).getCredentials() + " " + data.get(i).getBalance()+"\uD83D\uDCB2");

                    final int finalInt = i;

                    AtomicBoolean ticked1 = new AtomicBoolean(false);

                    btn.setOnClickListener(k -> {

                        if (!ticked1.get()){
                            Toast.makeText(getActivity(), "Are you sure? Press one more time to remove a friend.", Toast.LENGTH_SHORT).show();
                            ticked1.set(true);
                        }else{
                            try{
                                k.setEnabled(false);
                                this.loadAction("remove", data.get(finalInt).getFriendship_id());
                            }catch (RuntimeException e){
                                Log.e("FAILURE", e.getMessage());
                            }
                        }
                    });

                    innerLayout.addView(txt);
                    innerLayout.addView(btn);
                }

                layout.addView(innerLayout);
            } catch (ArrayIndexOutOfBoundsException exc) {
                Log.d("bounds_arr", exc.getMessage());
            }
        }
    }
}

