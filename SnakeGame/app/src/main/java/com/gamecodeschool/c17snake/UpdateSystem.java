package com.gamecodeschool.c17snake;

public class UpdateSystem {
    private long mNextFrameTime;
    private final long MILLIS_PER_SECOND = 1000;
    private int TARGET_FPS = 10; // Changed from final to non-final

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

    // New method to set the TARGET_FPS
    public void setTargetFPS(int targetFPS) {
        this.TARGET_FPS = targetFPS;
    }
}
