package com.example.ez_inventory_system;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;
import android.media.MediaPlayer;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private MediaPlayer bgmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bgmusic=MediaPlayer.create(this,R.raw.backgroundmusic);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView(this, point.x, point.y);

        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        bgmusic.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        bgmusic.start();
    }
}
