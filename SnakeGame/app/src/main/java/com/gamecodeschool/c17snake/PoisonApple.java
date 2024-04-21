package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class PoisonApple extends Apple {

    // An image to represent the apple
    private Bitmap mBitmapApple;

    public boolean spawned;

    // Maintain a single global reference to the apple
    private static PoisonApple poisonApple;

    /// Set up the apple in the constructor
    PoisonApple(Context context, Point sr, int s) {

        super(context, sr, s);

        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.poisonapple);

        // Resize the bitmap
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s * 2, s * 2, false);
    }

    // Template Getter
    public static GameObject customGet(Context context, Point sr, int s) {
        if (poisonApple == null) {
            poisonApple = new PoisonApple(context, sr, s);
        }
        return poisonApple;
    }

    // This is called from GameObject everytime an apple is eaten
    @Override
    public void spawn() {
        super.spawn();
        spawned = true;
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    // Draw the apple
    @Override
    public void draw(Canvas canvas, Paint paint) {
        //canvas.drawBitmap(mBitmapApple,
        //        location.x * mSize, location.y * mSize, paint);
        if (spawned) {
            canvas.drawBitmap(mBitmapApple,
                    location.x * size, location.y * size, paint);
        }
    }

    // Method to hide the apple
    @Override
    public void hide() {
        // Set the apple's location outside the visible screen
        location.set(-1, -1); // Set the location outside the visible screen
        spawned = false;
    }
}
