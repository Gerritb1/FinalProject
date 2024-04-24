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

        gameOverScreen = new GameOverScreen(this, snakeGame);

        setContentView(gameOverScreen);



    }
}