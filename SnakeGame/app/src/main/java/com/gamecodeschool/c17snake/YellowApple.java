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

class YellowApple extends Apple implements Spawnable{

    // An image to represent the apple
    private Bitmap mBitmapApple;

    public boolean spawned;

    // Maintain a single global reference to the apple
    private static YellowApple yellowApple;
    private static Point locations = null;
    private List<Point> rockLocations = new ArrayList<>();
    private List<Point> trashLocations = new ArrayList<>();
    private Point mAppleLocations = null;
    private Point pAppleLocations = null;
    private Point eDrinkLocations = null;
    /// Set up the apple in the constructor

        /// Set up the apple in the constructor
        private YellowApple(Context context, Point sr, int s) {

            super(context, sr, s);

            // Load the image to the bitmap
            mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowapple);

            // Resize the bitmap
            mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s * 2, s * 2, false);
        }

        // Provide access to the apple, creating it if necessary
        public static YellowApple getYellowApple(Context context, Point sr, int s) {
            if(yellowApple == null)
                yellowApple = new YellowApple(context, sr, s);
            return yellowApple;
        }

        // This is called every time an apple is eaten
        @Override
        public void spawn() {
            List<Point> rockLocations = Rock.get_Locations();
            Point mAppleLocation = Apple.get_Location();
            Point pAppleLocation = PoisonApple.get_Location();
            List<Point> trashLocations = Trash.get_Locations();
            Point eDrinkLocation = EnergyDrink.get_Location();

            Random random = new Random();
            location.x = random.nextInt(mSpawnRange.x) + 1;
            location.y = random.nextInt(mSpawnRange.y - 1) + 1;

            while (isLocationInvalid(location, rockLocations) ||
                    (isLocationInvalid(location, mAppleLocation)) ||
                    (isLocationInvalid(location, pAppleLocation)) ||
                    (eDrinkLocation != null && isLocationInvalid(location, eDrinkLocation)) ||
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

    private boolean isLocationInvalid(Point location, Point otherLocation) {
        if (otherLocation == null) return false;  //Needed to prevent crashing
        int xDiff = Math.abs(location.x - otherLocation.x);
        int yDiff = Math.abs(location.y - otherLocation.y);
        return xDiff <= 2 && yDiff <= 2;
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

    // Method to hide the apple
    @Override
    public void hide() {
        // Set the apple's location outside the visible screen
        location.set(-10, -10); // Set the location outside the visible screen
        spawned = false;
    }
    public static Point get_Location(){
        return locations;
    }
    public static void remove_Locations(){
        locations = null;
    }

}
