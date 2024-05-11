package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Apple extends GameObject implements Spawnable{

    // An image to represent the apple
    private Bitmap mBitmapApple;

    // Maintain a single global reference to the apple
    private static Apple apple;
    private static Point locations = null;

    private List<Point> rockLocations = new ArrayList<>();
    private List<Point> trashLocations = new ArrayList<>();
    private Point yAppleLocations = null;
    private Point pAppleLocations = null;

    private Point eDrinkLocations = null;
    private boolean spawned;

    /// Set up the apple in the constructor
    protected Apple(Context context, Point sr, int s) {

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
        List<Point> rockLocations = Rock.get_Locations();
        Point yAppleLocation = YellowApple.get_Location();
        Point pAppleLocation = PoisonApple.get_Location();
        Point eDrinkLocation = EnergyDrink.get_Location();
        List<Point> trashLocations = Trash.get_Locations();

        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;

        while (isLocationInvalid(location, rockLocations) ||
                (isLocationInvalid(location, yAppleLocation)) ||
                (isLocationInvalid(location, pAppleLocation)) ||
                (isLocationInvalid(location, eDrinkLocation)) ||
                isLocationInvalid(location, trashLocations)) {
            location.x = random.nextInt(mSpawnRange.x) + 1;
            location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        }

        locations = new Point(location.x, location.y);
    }

    private boolean isLocationInvalid(Point location, List<Point> otherLocations) {
        for (Point otherLocation : otherLocations) {
            if (otherLocation == null) continue;  //Needed to prevent crashing
            int xDiff = Math.abs(location.x - otherLocation.x);
            int yDiff = Math.abs(location.y - otherLocation.y);
            if (xDiff <= 2 && yDiff <= 2) {
                return true;
            }
        }
        return false;
    }

    //Overloaded method
    private boolean isLocationInvalid(Point location, Point otherLocation) {
        if (otherLocation == null) return false;  //Needed to prevent crashing
        int xDiff = Math.abs(location.x - otherLocation.x);
        int yDiff = Math.abs(location.y - otherLocation.y);
        return xDiff <= 2 && yDiff <= 2;
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
    public static Point get_Location(){
        return locations;
    }
    public static void remove_Locations(){
        locations = null;
    }
    public boolean isSpawned() {
        return spawned;
    }

}