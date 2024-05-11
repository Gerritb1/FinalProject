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

    protected EnergyDrink(Context context, Point sr, int size){
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
        List<Point> rockLocations = Rock.get_Locations();
        Point mAppleLocation = Apple.get_Location();
        Point yAppleLocation = YellowApple.get_Location();
        Point pAppleLocation = PoisonApple.get_Location();
        List<Point> trashLocations = Trash.get_Locations();

        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;

        while (isLocationInvalid(location, rockLocations) ||
                (isLocationInvalid(location, mAppleLocation)) ||
                (isLocationInvalid(location, yAppleLocation)) ||
                (isLocationInvalid(location, pAppleLocation)) ||
                isLocationInvalid(location, trashLocations)) {
            location.x = random.nextInt(mSpawnRange.x) + 1;
            location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        }

        locations = new Point(location.x, location.y);
        spawned = true;
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
        canvas.drawBitmap(mBitmapEnergyDrink,
                (location.x * size) - (scaledSize / 2), (location.y * size) - (scaledSize / 2), paint);
    }


    // Method to hide the apple
    @Override
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

