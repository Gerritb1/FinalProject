package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class YellowApple extends Apple {

    // An image to represent the yellow apple
    private Bitmap mBitmapYellowApple;

    public boolean spawned;

    // Maintain a single global reference to the yellow apple
    private static YellowApple yellowApple;

    /// Set up the yellow apple in the constructor
    YellowApple(Context context, Point sr, int s) {
        super(context, sr, s);

        // Load the image to the yellow apple bitmap
        mBitmapYellowApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowapple);

        // Resize the yellow apple bitmap
        mBitmapYellowApple = Bitmap.createScaledBitmap(mBitmapYellowApple, s * 2, s * 2, false);
    }

    // template method cus
    public static GameObject customGet(Context context, Point sr, int s) {
        if (yellowApple == null) {
            yellowApple = new YellowApple(context, sr, s);
        }
        return yellowApple;
    }

    // This is called every time a yellow apple is eaten
    @Override
    public void spawn() {
        super.spawn();
        spawned = true;
    }

    

@Override
    public boolean isSpawned() {
        return spawned;
    }

    // Draw the yellow apple
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (spawned) {
            canvas.drawBitmap(mBitmapYellowApple, location.x * size, location.y * size, paint);
        }
    }

    // Method to hide the yellow apple
    @Override
    public void hide() {
        // Set the yellow apple's location outside the visible screen
        location.set(-10, -10); // Set the location outside the visible screen
        spawned = false;
    }
}
