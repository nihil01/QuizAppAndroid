package com.example.quizapp.utils.playSound;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.quizapp.R;
import java.util.Random;

public class soundPlayer {
    private static final int[] soundRes = {
        R.raw.coin1,
        R.raw.coin2
    };

    public static void play(Context ctx) {
        try {
            Random rand = new Random();
            final MediaPlayer mp = MediaPlayer.create(ctx, soundRes[rand.nextInt(soundRes.length)]);
            if (mp != null) {
                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
            } else {
                Log.e("media_err", "MediaPlayer is null");
            }
        } catch (Exception e) {
            Log.e("media_err", e.getMessage());
        }
    }
}
