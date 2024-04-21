package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;


public abstract class ObjectAdapter {
    protected Point location;
    protected int mSegmentSize;
    protected int size;
    protected Point mSpawnRange;
    protected Point mMoveRange;
    protected ArrayList<Point> segmentLocations;

    // Constructor
    public ObjectAdapter(Context context, Point location, int size) {
        this.size = size;
        this.location = new Point(-10, -10);
        mSegmentSize = size;
        mSpawnRange = location;
        mMoveRange = location;
        segmentLocations = new ArrayList<>();
    }

    // Let SnakeGame know where the apple is
    // SnakeGame can share this with the snake
    public Point getLocation() {
        return location;
    }

    // Abstract method for drawing
    public abstract void draw(Canvas canvas, Paint paint);

    // Abstract method for hiding
    public abstract void hide();

    // Abstract method for spawning
    //public abstract void spawn();

}