package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.List;
import java.util.Random;

class PoisonApple extends Apple implements Spawnable{

    // An image to represent the apple
    private Bitmap mBitmapApple;

    public boolean spawned;

    // Maintain a single global reference to the apple
    private static PoisonApple poisonApple;

    /// Set up the apple in the constructor
    private PoisonApple(Context context, Point sr, int s) {

        super(context, sr, s);

        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.poisonapple);

        // Resize the bitmap
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s * 2, s * 2, false);
    }

    // Provide access to the apple, creating it if necessary
    public static PoisonApple getPoisonApple(Context context, Point sr, int s) {
        if(poisonApple == null)
            poisonApple = new PoisonApple(context, sr, s);
        return poisonApple;
    }

    // This is called every time an apple is eaten
    @Override
    public Point spawn(List<Point> rockLocations) {
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        for (int x=0; x<4; x++){
            if(location.x==rockLocations.get(x).x || location.y==rockLocations.get(x).y){
                location.x = random.nextInt(mSpawnRange.x) + 1;
                location.y = random.nextInt(mSpawnRange.y - 1) + 1;
            }
        }
        spawned = true;
        return new Point (-10, -10);
    }

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

}
