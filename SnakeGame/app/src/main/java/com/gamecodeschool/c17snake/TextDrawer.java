package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TextDrawer extends SurfaceView implements IText {

    private static Apple mApple;
    private static Snake mSnake;
    private DrawPauseButton getScreen;
    private final Canvas mCanvas;
    private final Paint mPaint;
    private final SnakeGame mSnakeGame;
    private int mScore;
    private int ss;
    private YellowApple yApple;
    private ArrayList<Rock> rocks;
    private Point mr;


    // Constructor of NameDrawer class with DI
    public TextDrawer(Context context, Canvas canvas, Paint paint, SnakeGame snakeGame) {
        super(context);
        this.mCanvas = canvas;
        this.mPaint = paint;
        this.mSnakeGame = snakeGame;
    }

    // Setter method for injecting DrawPauseButton dependency
    public void setDrawPauseButton(DrawPauseButton getScreen) {this.getScreen = getScreen; }
    public void setSnake(Snake snake){ mSnake = snake;}

    @Override //Extracted from SnakeGame
    public void drawColorSize() {

        Point screenDimensions = getScreen.getScreenDimensions();
        mSnake = Snake.getSnake(getContext(), mr, ss);
        setSnake(mSnake);
        // Ensure that mApple, mSnake, yApple, and rocks are not null before drawing
        if (mApple != null && mSnake != null && yApple != null && rocks != null) {
            // Set the size and color of the mPaint for the text

            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(120);

            // Draw the score
            mCanvas.drawText("" + mScore, 20, 120, mPaint);

            // Draw the apples, snake, and rocks
            mApple.draw(mCanvas, mPaint);
            yApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas,mPaint);

            for (Rock rock : rocks) {
                rock.draw(mCanvas, mPaint);
            }
        }
    }

    // Method for drawing names
    @Override
    public void drawNames() { //Extracted from SnakeGame
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
    public void drawTapToPlay() { //Extracted from SnakeGame
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
