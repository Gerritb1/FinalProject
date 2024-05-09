package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bomb extends GameObject implements Spawnable {

    private Bitmap mBitmapBomb;

    private final float mSpeedFactor = 0.3f;// Adjust this value to make the bomb move slower or faster
    private boolean shot = false;

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
    private Canvas mCanvas;

    private Paint mPaint;
    private SnakeGame mSnakeGame;


    public Bomb(Context context, Point snakeLocation, int size) {
        super(context, snakeLocation, size);
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, size, size, false);

        //Bomb spawns at the snake's location when shot
        mLocation = new Point(snakeLocation.x, snakeLocation.y);
        initializeSegmentLocations();
    }


    private void initializeSegmentLocations() {
        segmentLocations = new ArrayList<>();
        // Add an initial segment location to avoid null or empty list
        segmentLocations.add(new Point(mLocation.x, mLocation.y)); // Replace with actual initial values
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
            Log.d("Bomb", "Bomb is spawned. Spawned flag status: " + spawned);
        } else if (spawnedFiredBomb) {
            canvas.drawBitmap(mBitmapBomb, mLocation.x * size, mLocation.y * size, paint);
            Log.d("Bomb", "Bomb is fired. SpawnedFiredBomb flag status: " + spawnedFiredBomb);
        } else {
            Log.d("Bomb", "Bomb is not spawned or fired. Spawned flag status: " + spawned + ", SpawnedFiredBomb flag status: " + spawnedFiredBomb);
        }
    }



    public void shootBomb(boolean isTriggerButtonPressed, Point mLocation) {
        Log.d("Bomb", "shootBomb called");
        if (!(!isTriggerButtonPressed && isReadyToExplode())) {
            Log.d("Bomb", "Trigger button is pressed and bomb is not ready to explode");
            // Check if segmentLocations is not null and not empty
            if (segmentLocations != null && !segmentLocations.isEmpty()) {
                Log.d("Bomb", "segmentLocations is not null and not empty");
                // Set the bomb's initial location to the snake's mouth location
                setLocation(segmentLocations.get(0));
                Log.d("Bomb", "Initial bomb location set to: " + mLocation);

                // Get the snake's heading
                Point directionVector = Snake.getSnake(mContext, mLocation, size).getHeadPosition();

                // Check if directionVector is not null
                if (directionVector != null) {
                    Log.d("Bomb", "Direction vector: (" + directionVector.x + ", " + directionVector.y + ")");

                    // Set the shoot direction
                    mShootDirection = directionVector;
                    spawnFiredBomb(mShootDirection); //Spawn Moving bomb after fired
                    //  shot = false; //Reset shot to false
                    setShot(true);

                } else {
                    Log.d("Bomb", "directionVector is null, cannot shoot bomb.");
                }
            } else {
                Log.d("Bomb", "segmentLocations is null or empty, cannot shoot bomb.");
            }
        }
    }


    public void update() {
        Log.d("Bomb", "update called");
        if (spawnedFiredBomb) {
            Log.d("Bomb", "Bomb is spawned and fired");
            // Move the bomb in the direction it was fired
            mLocation.x += (int) (mShootDirection.x*mSpeedFactor);
            mLocation.y += (int) (mShootDirection.y*mSpeedFactor);
            Log.d("Bomb", "Bomb position updated to: " + mLocation);

            // Check if the bomb has completely gone off-screen
            if (mLocation.x < -size || mLocation.x > screenWidth + size || mLocation.y < -size || mLocation.y > screenHeight + size) {
                Log.d("Bomb", "Bomb went off-screen and is now hidden.");
                hideFiredBomb();
            }
        }
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
            // Reset the bomb's location to the snake's mouth
            mLocation.set(segmentLocations.get(0).x, segmentLocations.get(0).y); // Update bomb location to snake's mouth
            Log.d("Bomb", "Bomb respawned at: " + mLocation); // Log the bomb's new location

            // Set spawnedFiredBomb to true
            spawnedFiredBomb = true;
        }
    }

    public void hideFiredBomb() {
        // Move the bomb off-screen
        mLocation.set(-10,10);

        // Set spawnedFiredBomb to false
        spawnedFiredBomb = false;


    }




    public void updateSnakeLocation(Point newSnakeLocation) {
        if (!spawnedFiredBomb) { // Only update the bomb's location with the snake if the bomb has not been fired
            mLocation.set(newSnakeLocation.x, newSnakeLocation.y);
            segmentLocations.set(0, new Point(mLocation.x, mLocation.y));
        }
    }

    @Override
    public void hide() {
        // Set the snake's head position outside the visible screen
        location.set(-10,10); // Set the location outside the visible screen
        spawned = false;

    }

    public Point getShootDirection() { return mShootDirection; }

    public boolean isSpawned() {
        return spawned;
    }

    public boolean isReadyToExplode() {
        return readyToExplode;
    }

    public boolean isShot() {
        return this.shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public void setSpawnedFiredBomb(boolean spawnedFiredBomb) {
        this.spawnedFiredBomb = spawnedFiredBomb;
    }

}
