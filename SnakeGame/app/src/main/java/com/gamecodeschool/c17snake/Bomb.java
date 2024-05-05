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

    private void setTriggerButtonPressed(){ isTriggerButtonPressed = true;}

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
        if (motionEvent != null) {
            Log.d("Bomb", "motionEvent is not null");

            if (isTriggerButtonPressed) {
                Log.d("Bomb", "Trigger button is pressed");

                if (!isReadyToExplode()) {
                    Log.d("Bomb", "Bomb is not ready to explode");

                    int touchX = (int) motionEvent.getX();
                    int touchY = (int) motionEvent.getY();

                    if (getLocation() != null) {
                        Log.d("Bomb", "Location is not null");

                        int directionX = touchX - getLocation().x;
                        int directionY = touchY - getLocation().y;

                        mShootDirection = new Point(directionX, directionY);

                        getLocation().x += mShootDirection.x;
                        getLocation().y += mShootDirection.y;

                        if (segmentLocations != null) {
                            Log.d("Bomb", "segmentLocations is not null");

                            for (int i = segmentLocations.size() - 1; i > 0; i--) {
                                segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
                                segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
                            }
                        }
                    } else {
                        Log.d("Bomb", "Location is null");
                    }
                } else {
                    Log.d("Bomb", "Bomb is ready to explode");
                }
            } else {
                Log.d("Bomb", "Trigger button is not pressed");
            }
        } else {
            Log.d("Bomb", "motionEvent is null");
        }
    }
    public void setShootDirection(Point shootDirection) {
        this.mShootDirection = shootDirection;
    }

    public Point getShootDirection() {
        return mShootDirection;
    }

    public Point getLocation() {
        return mLocation;
    }

    public ArrayList<Point> getSegmentLocations() {
        return segmentLocations;
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
        if (snake.bigCheckDinner(location)) {
            readyToExplode = true;
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
