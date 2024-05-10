package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Rock extends GameObject implements Spawnable{

    // An image to represent the rock
    private Bitmap mBitmapRock;

    // Maintain a single global reference to the rock
    private static Rock rock1;
    private static Rock rock2;
    private static Rock rock3;
    private static Rock rock4;

    /// Set up the rock in the constructor
    Rock(Context context, Point sr, int s) {

        super(context, sr, s);

        // Load the image to the bitmap
        mBitmapRock = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);

        // Resize the bitmap
        mBitmapRock = Bitmap.createScaledBitmap(mBitmapRock, s * 2, s * 2, false);
    }

    // Provide access to the rock, creating it if necessary
    public static Rock getRock1(Context context, Point sr, int s) {
        if(rock1 == null)
            rock1 = new Rock(context, sr, s);
        return rock1;
    }
    public static Rock getRock2(Context context, Point sr, int s) {
        if(rock2 == null)
            rock2 = new Rock(context, sr, s);
        return rock2;
    }

    public static Rock getRock3(Context context, Point sr, int s) {
        if(rock3 == null)
            rock3 = new Rock(context, sr, s);
        return rock3;
    }

    public static Rock getRock4(Context context, Point sr, int s) {
        if(rock4 == null)
            rock4 = new Rock(context, sr, s);
        return rock4;
    }

    // This is called every time an rock is eaten
    @Override
    public void spawn() {
        // Choose two random values and place the rock
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }


    // Draw the rock
    @Override
    public void draw(Canvas canvas, Paint paint) {
        //canvas.drawBitmap(mBitmapRock,
        //        location.x * mSize, location.y * mSize, paint);
        canvas.drawBitmap(mBitmapRock,
                location.x * size, location.y * size, paint);
    }
}
