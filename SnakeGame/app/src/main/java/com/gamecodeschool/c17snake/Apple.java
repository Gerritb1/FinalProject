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
        // Choose two random values and place the apple
        List<Point> rockLocations = Rock.get_Locations();
        List<Point>trashLocations = Trash.get_Locations();
        Point yAppleLocations = YellowApple.get_Location();
        Point pAppleLocations = PoisonApple.get_Location();
        Point energyDrinkLocation = EnergyDrink.get_Location(); // Assuming you have a method to get the energy drink location
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        for (int x=0; x<4; x++){
            if((location.x==rockLocations.get(x).x || location.x==(rockLocations.get(x).x)+1 || location.x==(rockLocations.get(x).x)+2 || location.x==(rockLocations.get(x).x)-1 || location.x==(rockLocations.get(x).x)-2) && (location.y==rockLocations.get(x).y || location.y==(rockLocations.get(x).y+1) || location.y==(rockLocations.get(x).y+2) || location.y==(rockLocations.get(x).y-1) || location.y==(rockLocations.get(x).y-2))){
                location.x = random.nextInt(mSpawnRange.x) + 1;
                location.y = random.nextInt(mSpawnRange.y - 1) + 1;
            }
            if(!trashLocations.isEmpty()){
                if((location.x==trashLocations.get(x).x || location.x==(trashLocations.get(x).x)+1 || location.x==(trashLocations.get(x).x)-1) && (location.y==trashLocations.get(x).y || location.y==(trashLocations.get(x).y+1) || location.y==(trashLocations.get(x).y-1))){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
            if(yAppleLocations != null){
                if((location.x==yAppleLocations.x || location.x==yAppleLocations.x+1 || location.x==yAppleLocations.x-1) && (location.y==yAppleLocations.y || location.y==yAppleLocations.y+1 || location.y==yAppleLocations.y-1)){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
            if(pAppleLocations != null){
                if((location.x==pAppleLocations.x || location.x==pAppleLocations.x+1 || location.x==pAppleLocations.x-1) && (location.y==pAppleLocations.y || location.y==pAppleLocations.y+1 || location.y==pAppleLocations.y-1)){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
            if(energyDrinkLocation != null){
                if((location.x==energyDrinkLocation.x || location.x==energyDrinkLocation.x+1 || location.x==energyDrinkLocation.x-1) && (location.y==energyDrinkLocation.y || location.y==energyDrinkLocation.y+1 || location.y==energyDrinkLocation.y-1)){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
        }
        locations = new Point(location.x, location.y);
        spawned = true;
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
    public void hide() {
        // Set the apple's location outside the visible screen
        location.set(-1, -1); // Set the location outside the visible screens
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
