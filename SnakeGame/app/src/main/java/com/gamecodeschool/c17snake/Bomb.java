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
import java.util.List;
import java.util.Random;

public class Bomb extends GameObject implements Spawnable {

    private Bitmap mBitmapBomb;
    private static Bomb mBomb;
    private boolean readyToExplode = false;
    protected boolean spawned;
    private Point mShootDirection;
    private Point mLocation;
    private List<Point> segmentLocations;
    private Context mContext;
    private boolean spawnedFiredBomb = false;
    private int screenWidth;
    private int screenHeight;
    private Snake mSnake;


    public Bomb(Context context, Point snakeLocation, int size) {
        super(context, snakeLocation, size);
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, size, size, false);

        //Bomb spawns at the snake's location when shot
        mLocation = new Point(snakeLocation.x, snakeLocation.y); // Fixed
        initializeSegmentLocations();
    }


    private void initializeSegmentLocations() {
        segmentLocations = new ArrayList<>();
        // Add an initial segment location to avoid null or empty list
        segmentLocations.add(new Point(mLocation.x, mLocation.y)); // Replace with actual initial values
    }


    public void setSnake(Snake snake) {
        this.mSnake = snake;
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


    public void shootBomb(boolean isTriggerButtonPressed) {
        Log.d("Bomb", "shootBomb called");
        if (isTriggerButtonPressed && !isReadyToExplode()) {
            // Set the bomb's initial location to the snake's mouth location
            if (!segmentLocations.isEmpty()) {
                setLocation(segmentLocations.get(0));
                Log.d("Bomb", "Initial bomb location set to: " + mLocation);

                // Get the snake's heading
                Point directionVector = Snake.getSnake(mContext, mLocation, size).updatePosition();

                // Check if directionVector is null
                if (directionVector != null) {
                    Log.d("Bomb", "Direction vector: (" + directionVector.x + ", " + directionVector.y + ")");

                    // Set the shoot direction
                    mShootDirection = directionVector;
                    spawnFiredBomb(mShootDirection); //Spawn Moving bomb after fired
                } else {
                    Log.d("Bomb", "directionVector is null, cannot shoot bomb.");
                }
            } else {
                Log.d("Bomb", "segmentLocations is empty, cannot shoot bomb.");
            }
        }
    }

    public void update() {
        if (spawnedFiredBomb) {
            // Move the bomb in the direction it was fired
            mLocation.x += mShootDirection.x;
            mLocation.y += mShootDirection.y;
            Log.d("Bomb", "Bomb position updated to: " + mLocation);

            // Check if the bomb has gone off-screen
            if (mLocation.x < 0 || mLocation.x > screenWidth || mLocation.y < 0 || mLocation.y > screenHeight) {
                hide(); // Hide the bomb if it's off-screen
                Log.d("Bomb", "Bomb went off-screen and is now hidden.");
                spawnedFiredBomb = false; // Stop updating the bomb's position
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

    //Spawn fired bomb
// Update the bomb's location to where the snake is shooting the bomb from
    public void spawnFiredBomb(Point shootDirection) {
        if (shootDirection != null) {
            mLocation.set(segmentLocations.get(0).x, segmentLocations.get(0).y); // Update bomb location to snake's mouth
            mShootDirection = shootDirection;
            spawnedFiredBomb = true;
        }
    }

    // Check for collision with the snake
    public void checkSnakeCollision(Snake snake) {
        if (snake.checkDinner(location)) {
            readyToExplode = false;
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
