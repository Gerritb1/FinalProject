package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;

public class DrawPauseButton extends SurfaceView {

    private SnakeGame snakeGame;
    private int screenWidth;
    private int screenHeight;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonLeft;
    private int buttonTop;
    private float textWidth;
    private float textX;
    private float textY;

    // Maintain a single global reference to the DrawPauseButton
    private static DrawPauseButton drawPauseButton;

    private DrawPauseButton(Context context, SnakeGame snakeGame) {
        super(context);
        this.snakeGame = snakeGame;
    }

    // Provide access to the DrawPauseButton, creating it if necessary
    public static DrawPauseButton getDrawPauseButton(Context context, SnakeGame snakeGame) {
        if(drawPauseButton == null)
            drawPauseButton = new DrawPauseButton(context, snakeGame);
        return drawPauseButton;
    }

    public Point getScreenDimensions() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        return new Point(screenWidth, screenHeight);
    }

    public void drawButton(Canvas canvas, Paint paint) {

        drawButton(paint);

        // Draw a rounded rectangle representing the button
        canvas.drawRoundRect(
                buttonLeft, buttonTop,
                buttonLeft + buttonWidth, buttonTop + buttonHeight,
                25, 25, paint
        );

        // Set color and size for the button text
        paint.setColor(Color.WHITE);
        paint.setTextSize(buttonHeight * 0.9f);

        drawButtonText(canvas, paint);

    }

    //Method Overloading
    public void drawButton(Paint paint) {
        // Set color for the button background
        paint.setColor(Color.argb(255, 203, 67, 53));

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

    public void drawButtonText(Canvas canvas, Paint paint) {

        // Determine the text to be displayed based on the pause state
        String buttonText = snakeGame.isPaused() ? "Resume" : "Pause";

        // Calculate the position to center the text within the button
        textWidth = paint.measureText(buttonText);
        textX = buttonLeft + (buttonWidth - textWidth) / 2;
        textY = buttonTop + (float) buttonHeight / 2 + paint.getTextSize() / 3; // Adjust for vertical centering

        // Draw the determined text at the center of the button
        canvas.drawText(buttonText, textX, textY, paint);
    }
}
