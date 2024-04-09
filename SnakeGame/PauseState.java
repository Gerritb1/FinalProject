package com.gamecodeschool.c17snake;
import java.lang.Thread;
public class PauseState implements Statable {
    private SnakeGame snakeGame;

    private Thread mThread;
    private volatile boolean mPlaying = false;


    public PauseState(SnakeGame game) {
        this.snakeGame = game;
    }

    @Override
    public void enter() {
        snakeGame.setPaused(true); // Sets the game to paused state
    }

    @Override
    public void exit(){
        snakeGame.setPaused(false);
    }


    public void update() {
        // Handle any specific update logic for the paused state
    }

    // Add a method to resume the game
    public void resume() {
        if (!mPlaying) {
            // No explicit 'resume state'—simply resume the thread and gameplay
            mPlaying = true;
            mThread = new Thread((Runnable) this);
            mThread.start();
        }
    }
}
