package com.gamecodeschool.c17snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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

    private Bitmap background;




    public GameOverScreen(Context context, int score, int Highscore, Point size) {
        super(context);
        this.score = score;
        gContext = context;
        highscore = Highscore;
        mPrefs = context.getSharedPreferences("Highscore", 0);
        innitDrawing();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameoverbg);

        // Scale the image to match the screen size
        background = Bitmap.createScaledBitmap(background, size.x, size.y, true);
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
            gCanvas.drawBitmap(background, 0, 0, null);
            Typeface customFont = ResourcesCompat.getFont(context, R.font.retro);
            gPaint.setTypeface(customFont);

            gPaint.setColor(Color.RED);
            gPaint.setTextSize(75);
            gCanvas.drawText("GAME OVER", 800, 250, gPaint);

            gPaint.setColor(Color.WHITE);
            gPaint.setTextSize(50);
            gCanvas.drawText("Your Score: " + score, 850, 350, gPaint);

            gCanvas.drawText("Highscore:  " + highscore, 850, 410, gPaint);

            gCanvas.drawText("Tap To Play Again!", 550, 550, gPaint);


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