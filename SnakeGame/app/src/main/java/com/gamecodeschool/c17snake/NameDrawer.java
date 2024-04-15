package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

public class NameDrawer extends SurfaceView implements INames {
    private DrawPauseButton getScreen;
    private final Canvas mCanvas;
    private final Paint mPaint;
    private final SnakeGame mSnakeGame;

    // Constructor of NameDrawer class with DI
    public NameDrawer(Context context, Canvas canvas, Paint paint, SnakeGame snakeGame, DrawPauseButton getScreen) {
        super(context);
        this.mCanvas = canvas;
        this.mPaint = paint;
        this.mSnakeGame = snakeGame;
        this.getScreen = getScreen;
    }

    // Setter method for injecting DrawPauseButton dependency
    public void setDrawPauseButton(DrawPauseButton drawPauseButton) {
        this.getScreen = drawPauseButton;
    }

    // Method for drawing names
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
}
