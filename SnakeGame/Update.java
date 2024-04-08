package com.gamecodeschool.c17snake;

import android.media.SoundPool;


public class Update {
    // Example fields, adjust based on actual game requirements
    private Snake snake;
    private Apple apple;
    private SoundPool soundPool;
    private int eatSoundID;
    private boolean paused = false;
    private int score;
    private final long TARGET_FPS = 10;
    private final long MILLIS_PER_SECOND = 1000;
    private long mNextFrameTime;

   /* public Update(Snake snake, Apple apple, SoundPool soundPool, int eatSoundID, int score) {
        this.snake = snake;
        this.apple = apple;
        this.soundPool = soundPool;
        this.eatSoundID = eatSoundID;
        this.score = score;
        // Initialize mNextFrameTime for the first frame
        mNextFrameTime = System.currentTimeMillis();
    }

    // Method to encapsulate running update checks and execution
    public void runUpdate() {
        if (!paused && updateRequired()) {
            // Actual update logic
            snake.move();
            if (snake.checkDinner(apple.getLocation())) {
                apple.spawn();
                score++;
                soundPool.play(eatSoundID, 1, 1, 0, 0, 1);
            }
            if (snake.detectDeath()) {
                // Handle game reset or death scenario
            }
            // Additional update logic as needed
        }
    }*/

    protected boolean updateRequired() {
        if (mNextFrameTime <= System.currentTimeMillis()) {
            mNextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / TARGET_FPS;
            return true;
        }
        return false;
    }


}
