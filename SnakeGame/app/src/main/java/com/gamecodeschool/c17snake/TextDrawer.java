package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TextDrawer extends Screens{

    private static Apple mApple;
    private static Snake mSnake;
    private DrawPauseButton getScreen;
    private final Canvas mCanvas;
    private final Paint mPaint;
    private int mScore;
    private int ss;
    private YellowApple yApple;
    private ArrayList<Rock> rocks;
    private Point mr;


    // Constructor of NameDrawer class with DI
    public TextDrawer(Context context, Canvas canvas, Paint paint) {
        super(context);
        this.mCanvas = canvas;
        this.mPaint = paint;
    }

    // Method for drawing names
    public void drawNames() { //Extracted from SnakeGame
        Point screenDimensions = getScreenDimensions();
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

    public void drawTapToPlay() { //Extracted from SnakeGame
        // Draw the "Tap to play" message if the game is initially paused
        String message = getResources().getString(R.string.tap_to_play);
        Point centerPoint = getCenterPoint(getScreenDimensions(), getMessageDimensions(message, mPaint));

        // Draw the "Tap to play" message centered on the screen
        mCanvas.drawText(message, centerPoint.x, centerPoint.y, mPaint);
    }

}