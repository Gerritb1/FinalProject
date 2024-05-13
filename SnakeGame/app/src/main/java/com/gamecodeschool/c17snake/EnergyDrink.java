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
        // Choose two random values and place the apple
        List<Point> rockLocations = Rock.get_Locations();
        List<Point>trashLocations = Trash.get_Locations();
        Point yAppleLocations = YellowApple.get_Location();
        Point pAppleLocations = PoisonApple.get_Location();
        Point mAppleLocation = Apple.get_Location(); // Assuming you have a method to get the energy drink location
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
            if(mAppleLocation != null){
                if((location.x==mAppleLocation.x || location.x==mAppleLocation.x+1 || location.x==mAppleLocation.x-1) && (location.y==mAppleLocation.y || location.y==mAppleLocation.y+1 || location.y==mAppleLocation.y-1)){
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
