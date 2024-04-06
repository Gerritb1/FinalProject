package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Apple extends GameObject implements Spawnable{

    // An image to represent the apple
    private Bitmap mBitmapApple;

    // Maintain a single global reference to the apple
    private static Apple apple;

    /// Set up the apple in the constructor
    private Apple(Context context, Point sr, int s) {

        super(context, sr, s);

        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);

        // Resize the bitmap
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s, s, false);
    }

    // Provide access to the apple, creating it if necessary
    public static Apple getApple(Context context, Point sr, int s) {
        if(apple == null)
            apple = new Apple(context, sr, s);
        return apple;
    }

    // This is called every time an apple is eaten
    @Override
    public void spawn() {
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }


    // Draw the apple
    @Override
    public void draw(Canvas canvas, Paint paint) {
        //canvas.drawBitmap(mBitmapApple,
        //        location.x * mSize, location.y * mSize, paint);
        canvas.drawBitmap(mBitmapApple,
                location.x * size, location.y * size, paint);
    }

    // Method to hide the apple
    @Override
    public void hide() {
        // Set the apple's location outside the visible screen
        location.set(-1, -1); // Set the location outside the visible screen
    }

}
