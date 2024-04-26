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

    public GameOverScreen(Context context, int score) {
        super(context);
        drawGameOver(context, score);

    }


    private void drawGameOver(Context context, int score){
       SurfaceHolder gSurfaceHolder = getHolder();
       Canvas gCanvas = gSurfaceHolder.lockCanvas();
       Paint gPaint = new Paint();
       TextDrawer textDrawer = new TextDrawer(context, gCanvas, gPaint);

       gCanvas.drawColor(Color.BLACK);

       Typeface customFont = ResourcesCompat.getFont(context, R.font.retro);
       textDrawer.drawGameOver(customFont);
       textDrawer.drawScore(score, customFont);

       gSurfaceHolder.unlockCanvasAndPost(gCanvas);




    }

}
