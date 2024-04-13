package com.gamecodeschool.c17snake;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawBuilder {
    private Canvas mCanvas;
    private Paint mPaint;
    private String message;
    private boolean isFirstPause;
    private boolean mPaused;

   /* public DrawBuilder setCanvas(Canvas mCanvas) {
        this.mCanvas = mCanvas;
        return this;
    }

    public DrawBuilder setPaint(Paint mPaint) {
        this.mPaint = mPaint;
        return this;
    }

    public DrawBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public DrawBuilder setFirstPause(boolean isFirstPause) {
        this.isFirstPause = isFirstPause;
        return this;
    }

    public DrawBuilder setPaused(boolean mPaused) {
        this.mPaused = mPaused;
        return this;
    }

    public DrawBuilder buildDrawTapToPlay() {
        return new DrawTapToPlay(mCanvas, mPaint, message);
    }

    public DrawBuilder buildDrawNames() {
        return new DrawNames(mCanvas, mPaint, message);
    }

    public DrawBuilder buildCheckDrawConditions(DrawBuilder drawPaused, DrawBuilder drawNames) {
        return new CheckDrawConditions(isFirstPause, mPaused, drawPaused, drawNames);
    }*/
}