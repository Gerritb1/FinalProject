package com.gamecodeschool.c17snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ElseMap {

    // For tracking movement Heading
    public enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;


    private ElseMap elseMap;

    public ElseMap(ElseMap elseMap) {
        this.elseMap = elseMap;
    }

    public void populateElseMap(java.util.Map<Heading, HeadRotate> elseMap) {
        elseMap.put(Heading.UP, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.LEFT;
            }
        });
        elseMap.put(Heading.LEFT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.DOWN;
            }
        });
        elseMap.put(Heading.RIGHT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.UP;
            }
        });
        elseMap.put(Heading.DOWN, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.RIGHT;
            }
        });
    }
}