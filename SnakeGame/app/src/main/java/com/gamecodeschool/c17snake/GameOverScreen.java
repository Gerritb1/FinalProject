package com.gamecodeschool.c17snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private int score, highscore;
    SurfaceHolder gSurfaceHolder;
    Canvas gCanvas;
    Paint gPaint;
    TextDrawer textDrawer;
    volatile boolean playing;
    private Thread thread = null;
    SharedPreferences mPrefs;



    public GameOverScreen(Context context, int score, int Highscore) {
        super(context);
        this.score = score;
        gContext = context;
        highscore = Highscore;
        mPrefs = context.getSharedPreferences("Highscore", 0);
        innitDrawing();
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

            gCanvas.drawText("Highscore:  " + highscore, 650, 350, gPaint);

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
                Intent newGame = new Intent(gContext, SnakeActivity.class);
                gContext.startActivity(newGame);
                ((Activity) gContext).finishAffinity();
                ((Activity) gContext).overridePendingTransition(0,0);
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