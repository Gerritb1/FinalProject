package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;

import java.util.Random;

class Bomb extends GameObject implements Spawnable {

    private Bitmap mBitmapBomb;
    boolean spawned;
    private boolean readyToShoot;

    private Bitmap mBitmapApple;

    // Maintain a single global reference to the apple
    private static Apple apple;

    /// Set up the apple in the constructor
    protected Bomb(Context context, Point sr, int s) {
        super(context, sr, s);

        // Load the image to the bitmap
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);

        // Resize the bitmap to be bigger based on the provided size s
        int newSize = s * 2; // Increase the size by a factor of 2 (adjust this factor based on desired increase)
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, newSize, newSize, false);
    }

    private static Bomb bomb;

    public static Bomb getBomb(Context context, Point sr, int s) {
        if (bomb == null) {
            bomb = new Bomb(context, sr, s);
        }
        return bomb;
    }

    @Override
    public void spawn() {
        // Choose two random values and place the bomb
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }
    public boolean isSpawned() {
        return spawned;
    }

    private Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapBomb, location.x * size, location.y * size, paint);
    }

    @Override
    public void hide() {
        location.set(-10, -10);
    }

    // Method to set the ready to shoot flag
    public void setReadyToShoot(boolean readyToShoot) {
        this.readyToShoot = readyToShoot;
    }

}