package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;


import java.util.ArrayList;

public class TextDrawer extends Screens{
    private final Canvas mCanvas;
    private final Paint mPaint;



    // Constructor of NameDrawer class with DI
    public TextDrawer(Context context, Canvas canvas, Paint paint) {
        super(context);
        this.mCanvas = canvas;
        this.mPaint = paint;
    }

    // Method for drawing names
    public void drawNames(Typeface customFont) { //Extracted from SnakeGame
        mPaint.setTypeface(customFont);
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

    public void drawTapToPlay(Typeface customFont) { //Extracted from SnakeGame
        // Draw the "Tap to play" message if the game is initially paused
        mPaint.setTypeface(customFont);
        mPaint.setTextSize(175);
        mPaint.setColor(Color.RED);
        String message = getResources().getString(R.string.tap_to_play);
        Point centerPoint = getCenterPoint(getScreenDimensions(), getMessageDimensions(message, mPaint));

        // Draw the "Tap to play" message centered on the screen
        mCanvas.drawText(message, centerPoint.x, centerPoint.y, mPaint);
    }

    public void drawGameOver(Typeface customFont){
        mPaint.setColor(Color.RED);
        mPaint.setTypeface(customFont);
        mPaint.setTextSize(100);
        String message = getResources().getString(R.string.GameOver);

        Point centerPoint = getCenterPoint(getScreenDimensions(), getMessageDimensions(message, mPaint));

        mCanvas.drawText(message, centerPoint.x, centerPoint.y/4, mPaint );


    }

    public void drawScore(int score, Typeface customFont){
        mPaint.setColor(Color.WHITE);
        mPaint.setTypeface(customFont);
        mPaint.setTextSize(150);
        String scoreString = "" + score;

        mCanvas.drawText(scoreString, );
    }

}