package com.gamecodeschool.c17snake;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawObject {

    public static void drawSnake(Canvas canvas, Paint paint, Snake snake) {
        snake.draw(canvas, paint);
    }

    public static void drawApple(Canvas canvas, Paint paint, Apple apple) {
        apple.draw(canvas, paint);
    }
}
