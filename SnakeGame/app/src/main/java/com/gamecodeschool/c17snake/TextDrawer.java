package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

public class TextDrawer extends SurfaceView implements IText {
    private DrawPauseButton getScreen;
    private final Canvas mCanvas;
    private final Paint mPaint;
    private final SnakeGame mSnakeGame;

    // Constructor of NameDrawer class with DI
    public TextDrawer(Context context, Canvas canvas, Paint paint, SnakeGame snakeGame) {
        super(context);
        this.mCanvas = canvas;
        this.mPaint = paint;
        this.mSnakeGame = snakeGame;
    }

    // Setter method for injecting DrawPauseButton dependency
    public void setDrawPauseButton(DrawPauseButton getScreen) {
        this.getScreen = getScreen;
    }

    // Method for drawing names
    @Override
    public void drawNames() {
        Point screenDimensions = getScreen.getScreenDimensions();
        int screenWidth = screenDimensions.x;
        int xCoordinate = screenWidth - 340; // Adjust this value as needed

        mPaint.setColor(Color.argb(255, 255, 255, 255));
        mPaint.setTextSize(30);
        mCanvas.drawText(getResources().getString(R.string.name1), xCoordinate, 50, mPaint);
        mCanvas.drawText(getResources().getString(R.string.name2), xCoordinate, 85, mPaint);
        mCanvas.drawText(getResources().getString(R.string.name3), xCoordinate, 120, mPaint);
        mCanvas.drawText(getResources().getString(R.string.name4), xCoordinate, 155, mPaint);
        mCanvas.drawText(getResources().getString(R.string.name5), xCoordinate, 190, mPaint);
    }

    // Method for drawing "Tap to play" message
    @Override
    public void drawTapToPlay() {
        // Draw the "Tap to play" message if the game is initially paused
        String message = getResources().getString(R.string.tap_to_play);

        // Get the width and height of the message
        float messageWidth = mPaint.measureText(message);
        float messageHeight = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;

        // Get the screen dimensions
        Point screenDimensions = getScreen.getScreenDimensions();
        int screenWidth = screenDimensions.x;
        int screenHeight = screenDimensions.y;

        // Calculate the position to center the text horizontally and vertically
        float centerX = (screenWidth - messageWidth) / 2;
        float centerY = (screenHeight + messageHeight) / 2;

        // Draw the "Tap to play" message centered on the screen
        mCanvas.drawText(message, centerX, centerY, mPaint);
    }
}
