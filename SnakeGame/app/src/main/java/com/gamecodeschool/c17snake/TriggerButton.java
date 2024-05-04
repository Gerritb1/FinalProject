package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

public class TriggerButton extends SurfaceView {

    private int screenWidth;
    private int screenHeight;
    private int buttonWidth;
    private int buttonHeight;
    private int horizontalPosition;
    private int verticalPosition;

    private Snake mSnake;

    private Context mContext;
    private SnakeGame mSnakeGame;

    private Bomb mBomb;

    private static TriggerButton drawTriggerButton;

    public TriggerButton(Context context, SnakeGame snakeGame) {
        super(context);
        mContext = context;
        mSnakeGame = snakeGame;
    }

    public static TriggerButton getDrawTriggerButton(Context context, SnakeGame snakeGame) {
        if (drawTriggerButton == null)
            drawTriggerButton = new TriggerButton(context, snakeGame);
        return drawTriggerButton;
    }

    public void drawButton(Canvas canvas, Paint paint) {
        // Draw the circular transparent button
        drawTransparentButton(paint);

        // Set the same gray color as the PauseButton for the circular shape of the button
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(100, 203, 67, 53)); // Match the gray color from PauseButton
        canvas.drawCircle(horizontalPosition + buttonWidth / 2, verticalPosition + buttonHeight / 2, Math.min(buttonWidth, buttonHeight) / 2 - 5, paint); // Adjust the radius by subtracting 5

        // Draw the transparent bomb.png image scaled to fit the button
        Bitmap bombImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb);
        if (bombImage != null) {
            // Calculate the size to fit the image within the button
            float imageWidth = Math.min(buttonWidth, buttonHeight) * 0.8f; // Cast the result to float
            float imageHeight = imageWidth * ((float) bombImage.getHeight() / bombImage.getWidth());

            // Calculate the position to center the image within the button
            float imageLeft = horizontalPosition + (buttonWidth - imageWidth) / 2f + 10; // Adjusted to shift the image slightly to the right
            float imageTop = verticalPosition + (buttonHeight - imageHeight) / 2f;

            // Create a new paint object for drawing the transparent image
            Paint transparentPaint = new Paint();
            transparentPaint.setAlpha(150);

            // Draw the scaled and transparent bomb.png image centered on the button
            Bitmap scaledBombImage = Bitmap.createScaledBitmap(bombImage, (int) imageWidth, (int) imageHeight, true);
            canvas.drawBitmap(scaledBombImage, imageLeft, imageTop, transparentPaint);
        }
    }


    // Method to draw the circular transparent button
    private void drawTransparentButton(Paint paint) {
        // Set the same gray color as the PauseButton for the circular transparent button
        paint.setColor(Color.argb(100, 203, 67, 53)); // Match the gray color from PauseButton

        Point screenDimensions = getScreenDimensions();
        screenWidth = screenDimensions.x;
        screenHeight = screenDimensions.y;

        // Define the size and position of the button relative to screen dimensions
        buttonWidth = screenWidth / 10;
        buttonHeight = screenHeight / 10;

        horizontalPosition = screenWidth - buttonWidth - 150;
        verticalPosition = screenHeight - buttonHeight - 25;
    }

    public boolean contains(int x, int y) {
        int buttonLeft = horizontalPosition;
        int buttonRight = horizontalPosition + buttonWidth;
        int buttonTop = verticalPosition;
        int buttonBottom = verticalPosition + buttonHeight;

        return x >= buttonLeft && x <= buttonRight && y >= buttonTop && y <= buttonBottom;
    }

    private Point getScreenDimensions() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        return new Point(screenWidth, screenHeight);
    }

// In the TriggerButton class

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (contains(touchX, touchY)) {
                    // Pass the required arguments to getSnake method
                    mBomb = mBomb.getBomb(getContext(), new Point(touchX, touchY), 0);

                    // Calculate the shooting direction based on the touch coordinates within the SnakeGame class
                    Point direction = mBomb.calculateBombDirection(event);

                    // Shoot the bomb in the calculated direction
                    mBomb.shootBomb(direction);

                    return true; // Event handled
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
