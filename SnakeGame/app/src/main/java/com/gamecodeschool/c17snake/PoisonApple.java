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

class PoisonApple extends Apple implements Spawnable{

    // An image to represent the apple
    private Bitmap mBitmapApple;

    public boolean spawned;

    // Maintain a single global reference to the apple
    private static PoisonApple poisonApple;
    private static Point locations = null;
    private List<Point> rockLocations = new ArrayList<>();
    private List<Point> trashLocations = new ArrayList<>();
    private Point mAppleLocations = null;
    private Point yAppleLocations = null;
    private Point eDrinkLocations = null;

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
    public void spawn() {
        rockLocations = Rock.get_Locations();
        trashLocations = Trash.get_Locations();
        yAppleLocations = YellowApple.get_Location();
        mAppleLocations = Apple.get_Location();
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        for (int x=0; x<4; x++){
            if((location.x==rockLocations.get(x).x || location.x==(rockLocations.get(x).x)+1 || location.x==(rockLocations.get(x).x)+2 || location.x==(rockLocations.get(x).x)-1 || location.x==(rockLocations.get(x).x)-2) && (location.y==rockLocations.get(x).y || location.y==(rockLocations.get(x).y+1) || location.y==(rockLocations.get(x).y+2) || location.y==(rockLocations.get(x).y-1) || location.y==(rockLocations.get(x).y-2))){
                location.x = random.nextInt(mSpawnRange.x) + 1;
                location.y = random.nextInt(mSpawnRange.y - 1) + 1;
            }
            if((location.x==mAppleLocations.x || location.x==mAppleLocations.x+1 || location.x==mAppleLocations.x-1) && (location.y==mAppleLocations.y || location.y==mAppleLocations.y+1 || location.y==mAppleLocations.y-1)){
                location.x = random.nextInt(mSpawnRange.x) + 1;
                location.y = random.nextInt(mSpawnRange.y - 1) + 1;
            }
            if(yAppleLocations != null){
                if((location.x==yAppleLocations.x || location.x==yAppleLocations.x+1 || location.x==yAppleLocations.x-1) && (location.y==yAppleLocations.y || location.y==yAppleLocations.y+1 || location.y==yAppleLocations.y-1)){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
            if(!trashLocations.isEmpty()){
                if((location.x==trashLocations.get(x).x || location.x==(trashLocations.get(x).x)+1 || location.x==(trashLocations.get(x).x)-1) && (location.y==trashLocations.get(x).y || location.y==(trashLocations.get(x).y+1) || location.y==(trashLocations.get(x).y-1))){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
            if(eDrinkLocations != null){
                if((location.x==eDrinkLocations.x || location.x==eDrinkLocations.x+1 || location.x==eDrinkLocations.x-1) && (location.y==eDrinkLocations.y || location.y==eDrinkLocations.y+1 || location.y==eDrinkLocations.y-1)){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
        }
        locations = new Point(location.x, location.y);
        spawned = true;
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
        location.set(-1, -1); // Set the location outside the visible screen
        spawned = false;
    }
    public static Point get_Location(){
        return locations;
    }
    public static void remove_Locations(){
        locations = null;
    }
}
