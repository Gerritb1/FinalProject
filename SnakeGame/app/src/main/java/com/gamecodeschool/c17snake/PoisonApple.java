package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class PoisonApple extends AppleAdapter {

    // An image to represent the poison apple
    private Bitmap mBitmapPoisonApple;

    public boolean spawned;

    // Maintain a single global reference to the poison apple
    private static PoisonApple poisonApple;

    /// Set up the poison apple in the constructor
    PoisonApple(Context context, Point sr, int s) {
        super(context, sr, s);

        // Load the image to the poison apple bitmap
        mBitmapPoisonApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.poisonapple);

        // Resize the poison apple bitmap
        mBitmapPoisonApple = Bitmap.createScaledBitmap(mBitmapPoisonApple, s * 2, s * 2, false);
    }

    public static PoisonApple getPoisonApple(Context context, Point sr, int s) {
        if (poisonApple == null) {
            poisonApple = new PoisonApple(context, sr, s);
        }
        return poisonApple;
    }

    // This is called from GameObject every time a poison apple is eaten
    @Override
    public void spawn() {
        super.spawn();
        spawned = true;
    }

    public boolean isSpawned() {
        return spawned;
    }

    // Draw the poison apple
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (spawned) {
            canvas.drawBitmap(mBitmapPoisonApple, location.x * size, location.y * size, paint);
        }
    }

    // Method to hide the poison apple
    @Override
    public void hide() {
        // Set the poison apple's location outside the visible screen
        location.set(-1, -1); // Set the location outside the visible screen
        spawned = false;
    }
}