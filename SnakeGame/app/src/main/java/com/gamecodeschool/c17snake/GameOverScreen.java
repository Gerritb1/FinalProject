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

import androidx.core.content.res.ResourcesCompat;

public class GameOverScreen extends SurfaceView {
    private Canvas gCanvas;
    private Paint gPaint;
    private SurfaceHolder gSurfaceHolder;
    private TextDrawer textDrawer;
    private SnakeGame snakeGame;
    private Context gContext;



    public GameOverScreen(Context context, SnakeGame snakeGame) {
        super(context);
        gPaint = new Paint();
        gCanvas = new Canvas();
        gSurfaceHolder = getHolder();
        textDrawer = new TextDrawer(context, gCanvas, gPaint);
        this.snakeGame = snakeGame;
        gContext = context;

    }



    private void drawGameOver(){
       gCanvas = snakeGame.mSurfaceHolder.lockCanvas();
       gCanvas.drawColor(Color.BLACK);

       Typeface customFont = ResourcesCompat.getFont(gContext, R.font.retro);
       textDrawer.drawGameOver(customFont);



    }

    private SnakeGame getSnakeGame(){
        return snakeGame;
    }
}
