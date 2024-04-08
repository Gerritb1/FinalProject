package com.gamecodeschool.c17snake;

public class UpdateSystem {
    private long mNextFrameTime;
    private final long MILLIS_PER_SECOND = 1000;
    private final int TARGET_FPS = 10; // Example target FPS, adjust as needed

    public UpdateSystem() {
        // Initialize mNextFrameTime to ensure the first update occurs as intended
        mNextFrameTime = System.currentTimeMillis();
    }

    protected boolean updateRequired() {
        if (mNextFrameTime <= System.currentTimeMillis()) {
            // Calculate the time for the next update
            mNextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / TARGET_FPS;
            return true;
        }
        return false;
    }
}
