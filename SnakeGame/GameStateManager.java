package com.gamecodeschool.c17snake;

public class GameStateManager {
    private Statable currentState;
    private SnakeGame snakeGame;

    public GameStateManager(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        this.currentState = null; // Initialize with no state
    }

    public void setGameState(Statable newState) {
        switchState(newState);
    }

    public void setPauseState(Statable newState) {
        switchState(newState);
    }

    private void switchState(Statable newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        if (newState != null) {
            newState.enter();
        }
    }

    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }
}
