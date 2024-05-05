package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;


import java.util.ArrayList;
import java.util.Random;


public class Bomb extends GameObject implements Spawnable {

    private Bitmap mBitmapBomb;
    private static Bomb mBomb;
    private boolean readyToExplode = false;
    protected boolean spawned;
    private Point mShootDirection;
    private Point mLocation;

    private boolean isTriggerButtonPressed = false;

    public Bomb(Context context, Point location, int size) {
        super(context, location, size);
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, size, size, false);
        mLocation = new Point(0, 0); // Initialize mLocation with default values
    }

    private void setTriggerButtonPressed(boolean isTriggerButtonPressed){ this.isTriggerButtonPressed = isTriggerButtonPressed;}

    public static Bomb getBomb(Context context, Point location, int size) {
        if (mBomb == null) {
            mBomb = new Bomb(context, location, size);
        }
            return mBomb;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Draw the bomb image at the location of the snake's mouth
        if (spawned) {
            canvas.drawBitmap(mBitmapBomb, location.x * size, location.y * size, paint);
        }
        if (segmentLocations.size() > 0) {
            Point mouthLocation = segmentLocations.get(0);
            canvas.drawBitmap(mBitmapBomb, mouthLocation.x * size, mouthLocation.y * size, paint);
        }
    }

    // Add log statement to calculateBombDirection method
    public void shootBomb(MotionEvent motionEvent, boolean isTriggerButtonPressed) {
        if (motionEvent != null && isTriggerButtonPressed) {
            Log.d("Bomb", "motionEvent is not null and trigger button is pressed");

            if (!isReadyToExplode()) {
                Log.d("Bomb", "Bomb is not ready to explode");

                // Calculate the direction of the bomb based on the touch event
                int touchX = (int) motionEvent.getX();
                int touchY = (int) motionEvent.getY();

                // Assuming 'location' is the current position of the bomb
                if (location != null) {
                    Log.d("Bomb", "Location is not null");

                    // Calculate the direction vector for the bomb to move
                    int directionX = touchX - location.x;
                    int directionY = touchY - location.y;

                    // Normalize the direction vector (so the speed is constant)
                    double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
                    directionX = (int) (directionX / magnitude);
                    directionY = (int) (directionY / magnitude);

                    // Set the shoot direction
                    mShootDirection = new Point(directionX, directionY);

                    // Update the bomb's location
                    location.x += mShootDirection.x;
                    location.y += mShootDirection.y;

                    Log.d("Bomb", "segmentLocations is " + (segmentLocations == null ? "null" : "not null"));
                    Log.d("Bomb", "segmentLocations size is " + (segmentLocations == null ? "N/A" : segmentLocations.size()));

                    // If you have segments following the bomb, update their positions as well
                    if (segmentLocations != null && !segmentLocations.isEmpty()) {
                        Log.d("Bomb", "segmentLocations is not null");

                        // Update segment locations
                        for (int i = segmentLocations.size() - 1; i > 0; i--) {
                            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
                            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
                        }
                        // The first segment should follow the bomb's location
                        segmentLocations.get(0).x = location.x;
                        segmentLocations.get(0).y = location.y;
                    }
                } else {
                    Log.d("Bomb", "Location is null");
                }
            } else {
                Log.d("Bomb", "Bomb is ready to explode");
            }
        } else {
            Log.d("Bomb", "Either motionEvent is null or trigger button is not pressed");
        }
    }

    public Point getLocation() {
        return mLocation;
    }

    @Override
    public void spawn() {
            // Choose two random values and place the bomb
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        spawned = true;
    }

    // Check for collision with the snake
    public void checkSnakeCollision(Snake snake) {
        if (snake.checkDinner(location)) {
            readyToExplode = false;
            location.set(-10, -10); // Move the bomb off-screen
        }
    }

    @Override
    public void hide() {
        location.set(-10, -10);
    }
    public boolean isSpawned() {
        return spawned;
    }

    public boolean isReadyToExplode() {
        return readyToExplode;
    }
}
