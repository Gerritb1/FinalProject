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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Bomb extends GameObject implements Spawnable {

    private Bitmap mBitmapBomb;
    private static Bomb mBomb;
    private boolean readyToExplode = false;
    protected boolean spawned;
    private Point mShootDirection;
    private Point mLocation;
    private int bombCount;

    private List<Point> segmentLocations;
    private boolean spawnedFiredBomb = false;


    public Bomb(Context context, Point location, int size) {
        super(context, location, size);
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, size, size, false);
        mLocation = new Point(0, 0); // Initialize mLocation with default values
        initializeSegmentLocations();

    }

    private void initializeSegmentLocations() {
        segmentLocations = new ArrayList<>();
        // Add an initial segment location to avoid null or empty list
        segmentLocations.add(new Point(0, 0)); // Replace with actual initial values
    }



    // Getter and setter for mLocation
    public void setLocation(Point location) {
        this.mLocation = location;
    }

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
    }

    public void shootBomb(MotionEvent motionEvent, boolean isTriggerButtonPressed) {
        Log.d("Bomb", "shootBomb called");
        if (motionEvent != null && isTriggerButtonPressed && !isReadyToExplode()) {
            // Set the bomb's initial location to the snake's mouth location
            if (!segmentLocations.isEmpty()) {

                setLocation(segmentLocations.get(0));

                // Calculate the direction of the bomb based on the touch event
                int touchX = (int) motionEvent.getX();
                int touchY = (int) motionEvent.getY();

                // Calculate the direction vector for the bomb to move
                int directionX = touchX - mLocation.x;
                int directionY = touchY - mLocation.y;

                // Normalize the direction vector (so the speed is constant)
                double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
                directionX = (int) (directionX / magnitude);
                directionY = (int) (directionY / magnitude);

                // Set the shoot direction
                mShootDirection = new Point(directionX, directionY);
                spawnFiredBomb(mShootDirection); //Spawn Moving bomb after fired
            } else {
                Log.d("Bomb", "segmentLocations is empty, cannot shoot bomb.");
            }
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



    //Method Overloading
    public void spawnFiredBomb(Point shootDirection) {
        if (shootDirection != null) {
            location.set(mLocation.x, mLocation.y);
            mShootDirection = shootDirection;
            spawnedFiredBomb = true;
        }
    }

    // Check for collision with the snake
    public void checkSnakeCollision(Snake snake) {
        if (snake.checkDinner(location)) {
            readyToExplode = false;
            bombCount++;
            hide();
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
