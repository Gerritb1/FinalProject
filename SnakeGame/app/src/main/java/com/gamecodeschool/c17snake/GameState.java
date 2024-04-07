package com.gamecodeschool.c17snake;

public class GameState implements Statable {
    private SnakeGame snakeGame; // Reference to the main game class

    // Constructor takes a SnakeGame instance
    public GameState(SnakeGame game) {
        this.snakeGame = game;
    }

    @Override
    public void enter() {
        snakeGame.setPaused(false); // Ensures the game is not paused when entering this state
        // Additional setup for the game state can be added here
    }

    @Override
    public void update() {
        snakeGame.update(); // Calls the main game update logic
    }

    @Override
    public void exit() {
        // GameOver functionality
    }
}
