package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.Random;

public class Bomb extends GameObject implements Spawnable {

    private Bitmap mBitmapBomb;
    private static Bomb mBomb;
    private boolean readyToExplode = false;
    protected boolean spawned;
    private int screenWidth;
    private int screenHeight;

    public Bomb(Context context, Point location, int size) {
        super(context, location, size);
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, size, size, false);
    }

    public static Bomb getBomb(Context context, Point location, int size) {
        if (mBomb == null) {
            mBomb = new Bomb(context, location, size);
        }
        return mBomb;
    }

    // Setter method to update the screen width
    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    // Setter method to update the screen height
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Draw the bomb image at the location of the snake's mouth
        if (spawned) {
            canvas.drawBitmap(mBitmapBomb, location.x * size, location.y * size, paint);
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

    // Check for collision with the snake
    public void checkSnakeCollision(Snake snake) {
        if (snake.bigCheckDinner(location)) {
            readyToExplode = true;
            location.set(-10, -10); // Move the bomb off-screen
        }
    }

    // Calculate the shooting direction based on touch event
    public Point calculateBombDirection(MotionEvent motionEvent) {
        // Implement the logic to calculate the direction based on the touch event
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();

        // Calculate the direction vector from the touch coordinates
        int directionX = touchX - getLocation().x;
        int directionY = touchY - getLocation().y;

        return new Point(directionX, directionY);
    }

    // Update the bomb's position based on shooting direction
    public void shootBomb(Point direction) {
        // Set the bomb's new location based on the shooting direction
        int newX = getLocation().x + direction.x;
        int newY = getLocation().y + direction.y;

        // Check if the new position is within the bounds of the game screen
        if (newX >= 0 && newX < screenWidth && newY >= 0 && newY < screenHeight) {
            getLocation().x = newX;
            getLocation().y = newY;
        }
    }

    public boolean isSpawned() {
        return spawned;
    }

    public boolean isReadyToExplode() {
        return readyToExplode;
    }
}
