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

public class Trash extends Rock implements Spawnable{
    private Bitmap mBitmapTrash;
    private static Trash trash1;
    private static Trash trash2;
    private static Trash trash3;
    private static Trash trash4;
    private static List<Point> locations = new ArrayList<>();
    private List<Point> rockLocations = new ArrayList<>();
    private Point mAppleLocations;
    private Point pAppleLocations;
    private Point yAppleLocations;
    private Point eDrinkLocations;


    /// Set up the rock in the constructor
    protected Trash(Context context, Point sr, int s) {

        super(context, sr, s);

        // Load the image to the bitmap
        mBitmapTrash = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash);

        // Resize the bitmap
        mBitmapTrash= Bitmap.createScaledBitmap(mBitmapTrash, s * 2, s * 2, false);
    }

    // This is called when the chance spawn works
    @Override
    public void spawn() {
        rockLocations = Rock.get_Locations();
        yAppleLocations = YellowApple.get_Location();
        mAppleLocations = Apple.get_Location();
        pAppleLocations = PoisonApple.get_Location();
        eDrinkLocations = EnergyDrink.get_Location();
        // Choose two random values and place the trash
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        for (int x=0; x<4; x++){
            if((location.x==rockLocations.get(x).x || location.x==(rockLocations.get(x).x)+1 || location.x==(rockLocations.get(x).x)+2 || location.x==(rockLocations.get(x).x)-1 || location.x==(rockLocations.get(x).x)-2) && (location.y==rockLocations.get(x).y || location.y==(rockLocations.get(x).y+1) || location.y==(rockLocations.get(x).y+2) || location.y==(rockLocations.get(x).y-1) || location.y==(rockLocations.get(x).y-2))){
                location.x = random.nextInt(mSpawnRange.x) + 1;
                location.y = random.nextInt(mSpawnRange.y - 1) + 1;
            }
            if(locations.size() > x) {
                if ((location.x == locations.get(x).x || location.x == (locations.get(x).x) + 1 || location.x == (locations.get(x).x) - 1) && (location.y == locations.get(x).y || location.y == (locations.get(x).y + 1) || location.y == (locations.get(x).y - 1))) {
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
            if(eDrinkLocations != null){
                if((location.x==eDrinkLocations.x || location.x==eDrinkLocations.x+1 || location.x==eDrinkLocations.x-1) && (location.y==eDrinkLocations.y || location.y==eDrinkLocations.y+1 || location.y==eDrinkLocations.y-1)){
                    location.x = random.nextInt(mSpawnRange.x) + 1;
                    location.y = random.nextInt(mSpawnRange.y - 1) + 1;
                }
            }
        }
        locations.add(new Point(location.x, location.y));
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

public void chanceToSpawn(int score, int chance){
    Random rand = new Random();
    if (rand.nextInt(score) > chance) {
        this.spawn();
    }
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
public static List<Point> get_Locations(){
    return locations;
}
public static void remove_Locations(){
    locations = new ArrayList<>();
}

}
