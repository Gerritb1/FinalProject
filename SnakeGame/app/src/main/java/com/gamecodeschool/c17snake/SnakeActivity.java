package com.gamecodeschool.c17snake;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class SnakeActivity extends Activity {

    SnakeGame mSnakeGame;
    Bomb mBomb; // Declare the Bomb instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mSnakeGame = new SnakeGame(this, size);

        // Initialize the Bomb instance
        Point bombLocation = new Point(size.x / 2, size.y / 2); // Set the initial location of the bomb
        mBomb = Bomb.getBomb(this, bombLocation, 50); // Initialize the Bomb instance


        setContentView(mSnakeGame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSnakeGame.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSnakeGame.pause();
    }
}
