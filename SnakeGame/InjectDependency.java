package com.gamecodeschool.c17snake;

public class InjectDependency implements Injector {
    @Override
    public void injectDependencies(Object target) {
        if (target instanceof SnakeActivity) {
            // Inject dependencies into SnakeActivity
            SnakeActivity snakeActivity = (SnakeActivity) target;
            // Add code here to inject dependencies into SnakeActivity, if needed
        } else if (target instanceof SnakeGame) {
            // Inject dependencies into SnakeGame
            SnakeGame snakeGame = (SnakeGame) target;
            // Add code here to inject dependencies into SnakeGame, if needed
        }
        // Add more conditions for other classes if needed
    }
}


