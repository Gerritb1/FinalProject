package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.List;
import java.util.Random;

public class EnergyDrink extends GameObject implements Spawnable{

    private static EnergyDrink energyDrink;
    private Bitmap mBitmapEnergyDrink;
    //private static Point mLocation;
    private static Point locations = null;
    boolean spawned;
    private int scaledSize;

    private EnergyDrink(Context context, Point sr, int size){
        super(context, sr, size);

        mBitmapEnergyDrink = BitmapFactory.decodeResource(context.getResources(), R.drawable.energydrink);

        // Resize the bitmap
        mBitmapEnergyDrink = Bitmap.createScaledBitmap(mBitmapEnergyDrink, size*2, size*2, false);
    }



    public static EnergyDrink getEnergyDrink(Context context, Point sr, int size) {
        if(energyDrink == null)
            energyDrink = new EnergyDrink(context, sr, size);
        return energyDrink;
    }

    @Override
    public void spawn() {
        // Get the current locations of all game objects
        List<Point> rockLocations = Rock.get_Locations();
        Point mAppleLocation = Apple.get_Location();
        Point yAppleLocation = YellowApple.get_Location();
        Point pAppleLocation = PoisonApple.get_Location();
        List<Point> trashLocations = Trash.get_Locations();

        // Create a new random object
        Random random = new Random();

        // Generate a random location for the new object
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;

        // Check if the new location is invalid (i.e., it overlaps with an existing object)
        // If it is, generate a new location
        while (isLocationInvalid(location, rockLocations) ||
                (isLocationInvalid(location, mAppleLocation)) ||
                (isLocationInvalid(location, yAppleLocation)) ||
                (isLocationInvalid(location, pAppleLocation)) ||
                isLocationInvalid(location, trashLocations)) {
            location.x = random.nextInt(mSpawnRange.x) + 1;
            location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        }

        // Set the new location and mark the object as spawned
        locations = new Point(location.x, location.y);
        spawned = true;
    }

    private boolean isLocationInvalid(Point location, List<Point> otherLocations) {

        // Check if the new location is too close to any of the existing locations
        for (Point otherLocation : otherLocations) {
            if (otherLocation == null) continue;
            // 2-D Absolute Difference |x_2 - x_1| & |y_2 - y_1| <= 2
            int xDiff = Math.abs(location.x - otherLocation.x);
            int yDiff = Math.abs(location.y - otherLocation.y);
            if (xDiff <= 2 && yDiff <= 2) {
                return true; //Do not spawn here
            }
        }
        return false; //Spawn here
    }

    //Overloaded method
    private boolean isLocationInvalid(Point location, Point otherLocation) {
        // Check if the new location is too close to the other location
        if (otherLocation == null) return false;
        // 2-D Absolute Difference |x_2 - x_1| & |y_2 - y_1| <= 2
        int xDiff = Math.abs(location.x - otherLocation.x);
        int yDiff = Math.abs(location.y - otherLocation.y);
        return xDiff <= 2 && yDiff <= 2;
    }

    // Draw the apple
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapEnergyDrink,
                (location.x * size) - ((float) scaledSize / 2), (location.y * size) - ((float) scaledSize / 2), paint);
    }


    // Method to hide the apple
    public void hide() {
        location.set(-10, -10);
        spawned = false;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public static Point get_Location(){
        return locations;
    }

}

