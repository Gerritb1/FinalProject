package com.gamecodeschool.c17snake;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

public class GameOverScreen extends SurfaceView implements Runnable {
    Context gContext;
    int score;
    SurfaceHolder gSurfaceHolder;
    Canvas gCanvas;
    Paint gPaint;
    TextDrawer textDrawer;
    volatile boolean playing;
    private Thread thread = null;
    public GameOverScreen(Context context, int score) {
        super(context);
        this.score = score;
        this.gContext = context;

        innitDrawing();
    }
    public int getHighscore(int score){
        if(score > GameOverActivity.Highscore)
            GameOverActivity.Highscore = score;
        return GameOverActivity.Highscore;

    }

    public void innitDrawing(){
        gCanvas = new Canvas();
        gPaint = new Paint();
        gSurfaceHolder = getHolder();
        gPaint = new Paint();
        textDrawer = new TextDrawer(gContext, gCanvas, gPaint);
    }


    private void drawGameOver(Context context, int score){
        if(gSurfaceHolder.getSurface().isValid()) {
            gCanvas = gSurfaceHolder.lockCanvas();
            gCanvas.drawColor(Color.BLACK);
            Typeface customFont = ResourcesCompat.getFont(context, R.font.retro);
            gPaint.setTypeface(customFont);

            gPaint.setColor(Color.RED);
            gPaint.setTextSize(150);
            gCanvas.drawText("GAME OVER", 550, 150, gPaint);

            gPaint.setColor(Color.WHITE);
            gPaint.setTextSize(75);
            gCanvas.drawText("Your Score: " + score, 650, 250, gPaint);

            gCanvas.drawText("Highscore:  " + getHighscore(score), 650, 350, gPaint);

            gCanvas.drawText("Tap To Play Again!", 550, 500, gPaint);


//            gPaint.setColor(Color.WHITE);
//            gPaint.setTypeface(customFont);
//            gPaint.setTextSize(150);
//            String scoreString = "" + score;
//            String scoreMessage = "Your Score: ";
//            gCanvas.drawText(" your score: ", 50, 50, gPaint);

            //textDrawer.drawGameOver(customFont);
            //textDrawer.drawScore(score, customFont);

            gSurfaceHolder.unlockCanvasAndPost(gCanvas);
        }

    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            if(gContext instanceof Activity){
                SnakeGame.activityFlag = true;
                ((Activity) gContext).finish();
            }
        }
        return true;
    }

    public void pause() {
        playing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }

    }

    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        while (playing) {
            drawGameOver(gContext, score);
        }
    }


}