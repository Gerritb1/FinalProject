package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Bomb extends GameObject implements Spawnable{

    private Bitmap mBitmapBomb;

    private static Bomb mBomb;
    private boolean readyToExplode = false;
    protected boolean spawned;

    public Bomb(Context context, Point location, int size) {
        super(context, location, size);
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, size, size, false);
    }

    // Provide access to the bomb, creating it if necessary
    public static Bomb getBomb(Context context, Point location, int size) {
        if (mBomb == null) {
            mBomb = new Bomb(context, location, size);
        }
        return mBomb;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Draw the bomb image at the location of the snake's mouth

        if (spawned) {
            canvas.drawBitmap(mBitmapBomb,
                    location.x * size, location.y * size, paint);
        }
        if (segmentLocations.size() > 0) {
            Point mouthLocation = segmentLocations.get(0);
            canvas.drawBitmap(mBitmapBomb, mouthLocation.x * size, mouthLocation.y * size, paint);
        }
    }

    @Override
    public void spawn() {
        // Choose two random values and place the bomb
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        spawned = true;
    }

    @Override
    public void hide() {
        location.set(-10, -10);
    }


    public void checkSnakeCollision(Snake snake) {
        if (snake.bigCheckDinner(location)) {
            readyToExplode = true;
            location.set(-10, -10); // Move the bomb off-screen
        }
    }

        // Define the calculateBombDirection method to determine the shooting direction
    Point calculateBombDirection(MotionEvent motionEvent) {
        
        // Implement the logic to calculate the direction based on the touch event
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();

        // Calculate the direction vector from the touch coordinates
        int directionX = touchX - mBomb.getLocation().x; 
        int directionY = touchY - mBomb.getLocation().y;

        return new Point(directionX, directionY);
    }

    public void shootBomb(Point direction) {
        // Set the bomb's new location based on the shooting direction
        // Update the bomb's position in the game
        location.x += direction.x;
        location.y += direction.y;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public boolean isReadyToExplode() {
        return readyToExplode;
    }
}
