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
    private Point mAppleLocation = null;
    private Point yAppleLocation = null;
    private Point pAppleLocation = null;
    private Point eDrinkLocation = null;


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
        mAppleLocation = Apple.get_Location();
        yAppleLocation = YellowApple.get_Location();
        pAppleLocation = PoisonApple.get_Location();
        eDrinkLocation = EnergyDrink.get_Location();

        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;

        while (isLocationInvalid(location, rockLocations) ||
                isLocationInvalid(location, mAppleLocation) ||
                isLocationInvalid(location, yAppleLocation) ||
                isLocationInvalid(location, pAppleLocation) ||
                isLocationInvalid(location, eDrinkLocation)) {
            location.x = random.nextInt(mSpawnRange.x) + 1;
            location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        }

        locations.add(new Point(location.x, location.y));
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

