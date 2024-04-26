package com.gamecodeschool.c17snake;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;


public class GameOverActivity extends Activity {

    private GameOverScreen gameOverScreen;
    private SnakeGame snakeGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();

        // Initialize the result into a Point object
        Point size = new Point();
        int score = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }

        gameOverScreen = new GameOverScreen(this, score);

        setContentView(gameOverScreen);

    }
}