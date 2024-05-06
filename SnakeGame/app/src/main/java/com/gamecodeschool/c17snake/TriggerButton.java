package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
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

    private Context mContext;
    private SnakeGame mSnakeGame;

    private static TriggerButton drawTriggerButton;

    private boolean isTriggerButtonPressed = false;

    public void setPressed(boolean isTriggerButtonPressed) {
        this.isTriggerButtonPressed = isTriggerButtonPressed;
    }

    public TriggerButton(Context context, SnakeGame snakeGame) {
        super(context);
        mContext = context;
        mSnakeGame = snakeGame;
    }

    public static TriggerButton getDrawTriggerButton(Context context, SnakeGame snakeGame) {
        if (drawTriggerButton == null) {
            drawTriggerButton = new TriggerButton(context, snakeGame);
        }
        return drawTriggerButton;
    }

    public Rect getTriggerButtonRect() {
        if (mSnakeGame != null) {
            return mSnakeGame.mTriggerButtonRect;
        }
        return null; // Handle the case where SnakeGame reference is not set
    }

    public void drawButton(Canvas canvas, Paint paint) {
        // Draw the circular transparent button
        drawButton(paint);

        // Set the paint style to fill
        paint.setStyle(Paint.Style.FILL);

        // Draw a circle at the center of the button with a radius
        canvas.drawCircle(horizontalPosition + buttonWidth / 2, verticalPosition + buttonHeight / 2, Math.min(buttonWidth, buttonHeight) / 2 - 5, paint);

        // Draw the transparent bomb.png image scaled to fit the button
        Bitmap bombImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb);
        if (bombImage != null) {
            // Calculate the size to fit the image within the button
            float imageWidth = Math.min(buttonWidth, buttonHeight) * 0.8f;
            float imageHeight = imageWidth * ((float) bombImage.getHeight() / bombImage.getWidth());

            // Calculate the position to center the image within the button
            float imageLeft = horizontalPosition + (buttonWidth - imageWidth) / 2f + 10;
            float imageTop = verticalPosition + (buttonHeight - imageHeight) / 2f;

            // Create a new paint object for drawing the transparent image
            Paint transparentPaint = new Paint();
            transparentPaint.setAlpha(150);

            // Draw the scaled and transparent bomb.png image centered on the button
            Bitmap scaledBombImage = Bitmap.createScaledBitmap(bombImage, (int) imageWidth, (int) imageHeight, true);
            canvas.drawBitmap(scaledBombImage, imageLeft, imageTop, transparentPaint);
        }
    }

    public void drawButton(Paint paint) {
        // Set the color
        paint.setColor(Color.argb(100, 203, 67, 53));

        Point screenDimensions = getScreenDimensions();
        screenWidth = screenDimensions.x;
        screenHeight = screenDimensions.y;

        // Define the size relative to screen dimensions
        buttonWidth = screenWidth / 10;
        buttonHeight = screenHeight / 10;

        // Set the position of the button
        horizontalPosition = screenWidth - buttonWidth - 150;
        verticalPosition = screenHeight - buttonHeight - 25;

        // Log the position of the button
        Log.d("TriggerButton", "Button Position - Horizontal: " + horizontalPosition + ", Vertical: " + verticalPosition);
    }

    Point getScreenDimensions() {
        // Access the WindowManager service using the application context
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        // Get the default display of the WindowManager
        Display display = wm.getDefaultDisplay();

        // Initialize a DisplayMetrics object to store display metrics
        DisplayMetrics metrics = new DisplayMetrics();

        // Retrieve the display metrics of the default display
        display.getMetrics(metrics);

        // Extract the screen width and height from the metrics and assign them to screenWidth and screenHeight
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        // Return a new Point object containing the screen width and height
        return new Point(screenWidth, screenHeight);
    }

}
