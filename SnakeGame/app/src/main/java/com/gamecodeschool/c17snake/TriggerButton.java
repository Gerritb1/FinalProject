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
    private int horizontalPosition;
    private int verticalPosition;

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

        // Draw the circular shape for the button
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(horizontalPosition + buttonWidth/2, verticalPosition + buttonHeight/2, Math.min(buttonWidth, buttonHeight)/2, paint);

        // Draw the bomb.png image traced onto the button
        Bitmap bombImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb);
        if (bombImage != null) {
            // Calculate the position to center the image within the button
            float imageLeft = horizontalPosition + (buttonWidth - bombImage.getWidth()) / 2;
            float imageTop = verticalPosition + (buttonHeight - bombImage.getHeight()) / 2;
            canvas.drawBitmap(bombImage, imageLeft, imageTop, paint);
        }
    }

    // Method to draw the circular transparent button
    private void drawTransparentButton(Paint paint) {
        // Adjust the transparency level by changing the alpha value (50 to 150 for example)
        paint.setColor(Color.argb(100, 203, 67, 53));
        // Get screen dimensions
        Point screenDimensions = getScreenDimensions();
        screenWidth = screenDimensions.x;
        screenHeight = screenDimensions.y;

        // Define the size and position of the button relative to screen dimensions
        // Adjust the buttonWidth and buttonHeight to make the button smaller
        buttonWidth = screenWidth / 15; // Adjust the factor to determine the width
        buttonHeight = screenHeight / 10; // Adjust the factor to determine the height

        // Position the button at the bottom left corner of the screen
        horizontalPosition = screenWidth - buttonWidth - 100; // Set a margin from the left side of the screen
        verticalPosition = screenHeight - buttonHeight - 25; // Set a margin from the bottom of the screen
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
