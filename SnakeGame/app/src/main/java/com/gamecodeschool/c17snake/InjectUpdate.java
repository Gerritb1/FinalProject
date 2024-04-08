package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Point;

public class InjectUpdate {

    private Context context;
    private Point mr; // Assuming this is a measure for game area or something similar
    private int ss;

    public InjectUpdate(Context context, Point mr, int ss) {
        this.context = context;
        this.mr = mr;
        this.ss = ss;
    }

     public void injectDependencies(Object target) {
        if (target instanceof SnakeActivity) {
            // Inject dependencies into SnakeActivity
            SnakeActivity snakeActivity = (SnakeActivity) target;
        } else if (target instanceof SnakeGame) {
            // Create instances of Snake and Apple
            Snake snake = new Snake(context, mr, ss);
            Apple apple = new Apple(context, mr, ss);

            // Inject dependencies into SnakeGame
            SnakeGame snakeGame = (SnakeGame) target;
           // snakeGame.setUpdate(update);
        }
    }
}
