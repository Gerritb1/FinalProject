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
import android.view.SurfaceView;
import android.view.WindowManager;

public class TriggerButton extends SurfaceView {

    private int screenWidth;
    private int screenHeight;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonLeft;
    private int buttonTop;

    private Context mContext;
    private SnakeGame mSnakeGame;

    private static TriggerButton drawTriggerButton;

    public TriggerButton(Context context, SnakeGame snakeGame) {
        super(context);
        mContext = context;
        mSnakeGame = snakeGame;
    }

    public static TriggerButton getDrawTriggerButton(Context context, SnakeGame snakeGame) {
        if(drawTriggerButton == null)
            drawTriggerButton = new TriggerButton(context, snakeGame);
        return drawTriggerButton;
    }

    public void drawButton(Canvas canvas, Paint paint) {
        // Draw the circular transparent button
        drawTransparentButton(paint);

        // Draw the bomb.png image traced onto the button
        Bitmap bombImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb);
        if (bombImage != null) {
            // Calculate the position to center the image within the button
            float imageLeft = buttonLeft + (buttonWidth - bombImage.getWidth()) / 2;
            float imageTop = buttonTop + (buttonHeight - bombImage.getHeight()) / 2;
            canvas.drawBitmap(bombImage, imageLeft, imageTop, paint);
        }
    }

    // Method to draw the circular transparent button
    private void drawTransparentButton(Paint paint) {
        // Set color for the transparent button background
        paint.setColor(Color.argb(50, 255, 0, 0));

        // Get screen dimensions
        Point screenDimensions = getScreenDimensions();
        screenWidth = screenDimensions.x;
        screenHeight = screenDimensions.y;

        // Define the size and position of the button relative to screen dimensions
        buttonWidth = screenWidth / 9;
        buttonHeight = screenHeight / 20;
        buttonLeft = (screenWidth - buttonWidth) / 2;
        buttonTop = screenHeight / 10;
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
}