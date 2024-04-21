package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

public class Trash extends ObjectAdapter{
    private Bitmap mBitmapTrash;
    private static Trash trash1;
    private static Trash trash2;
    private static Trash trash3;
    private static Trash trash4;

    /// Set up the rock in the constructor
    private Trash(Context context, Point sr, int s) {

        super(context, sr, s);

        // Load the image to the bitmap
        mBitmapTrash = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash);

        // Resize the bitmap
        mBitmapTrash= Bitmap.createScaledBitmap(mBitmapTrash, s * 2, s * 2, false);
    }

    // This is called when the chance spawn works
    public void spawn() {
        // Choose two random values and place the trash
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }


    // Draw the trash
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapTrash,
                location.x * size, location.y * size, paint);
    }

    @Override
    public void hide() {
        // Set the trash's location outside the visible screen
        location.set(-10, -10); // Set the location outside the visible screen
    }

    public boolean chanceToSpawn(int score){
        Random rand = new Random();
        return rand.nextInt(score) > 3;
    }

    // Provide access to the trash, creating it if necessary
    public static Trash getTrash1(Context context, Point sr, int s) {
        if(trash1 == null)
            trash1 = new Trash(context, sr, s);
        return trash1;
    }
    public static Trash getTrash2(Context context, Point sr, int s) {
        if(trash2 == null)
            trash2 = new Trash(context, sr, s);
        return trash2;
    }

    public static Trash getTrash3(Context context, Point sr, int s) {
        if(trash3 == null)
            trash3 = new Trash(context, sr, s);
        return trash3;
    }

    public static Trash getTrash4(Context context, Point sr, int s) {
        if(trash4 == null)
            trash4 = new Trash(context, sr, s);
        return trash4;
    }

}
