package com.gamecodeschool.c17snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;


public class GameOverActivity extends Activity {

    private GameOverScreen gameOverScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();
        SnakeActivity.mSnakeGame.spawnHide();
        SnakeActivity.mSnakeGame.resetGameState();

        // Initialize the result into a Point object
        Point size = new Point();
        display.getSize(size);
        int score = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(score > sharedPref.getInt("scoreKey", 0)) {
            editor.putInt("scoreKey", score);
            editor.apply();
        }

        int Highscore = sharedPref.getInt("scoreKey", 0);


        gameOverScreen = new GameOverScreen(this, score, Highscore, size);

        setContentView(gameOverScreen);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameOverScreen.resume();
    }

    // Stop the thread in snakeEngine
    @Override
    protected void onPause() {
        super.onPause();
        gameOverScreen.pause();
    }

}