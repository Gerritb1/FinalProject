package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameOverScreen extends SurfaceView {
    private SnakeGame snakeGame;
    private Canvas gCanvas;
    private Paint gPaint;
    private SurfaceHolder gSurfaceHolder;
    private Typeface customFont;
    private TextDrawer textDrawer;



    public GameOverScreen(Context context, Point size) {
        super(context);
        gPaint = new Paint();
        gCanvas = new Canvas();
        gSurfaceHolder = getHolder();
        textDrawer = new TextDrawer(context, gCanvas, gPaint);

    }



    private void drawGameOver(){
       gCanvas = snakeGame.mSurfaceHolder.lockCanvas();

       gCanvas.drawColor(Color.BLACK);
       gPaint.setColor(Color.RED);
       String message = "a";
       //Point tst = textDrawer.getMessageDimensions(message);






    }

    private SnakeGame getSnakeGame(){
        return snakeGame;
    }
}
