package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;

public abstract class Screens extends SurfaceView {

    public Screens(Context context) {
        super(context);

    }

    //returns Pointer object <x,y>
    //X represents the width of the screen
    //Y represents the height of the screen
    protected Point getScreenDimensions(){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }

    //Returns a Pointer object <x,y>
    //X represents the width of the string
    //Y represents the height of the string
    protected Point getMessageDimensions(String message, Paint sPaint){
        return new Point((int) sPaint.measureText(message), (int) (sPaint.getFontMetrics().bottom - sPaint.getFontMetrics().top));
    }

    //Returns a Pointer object <x,y>
    //X represents the coordinate that centers a message horizontally
    //Y represents the coordinate that centers a message vertically
    protected Point getCenterPoint(Point screenDimensions, Point messageDimensions){
        return new Point(((screenDimensions.x - messageDimensions.x)/2), (screenDimensions.y + messageDimensions.y/2) / 2);
    }
}
