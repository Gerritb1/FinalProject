package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Point;

public interface Game {

    void fontTryCatch(Context context);

    void loadBackgroundImage(Context context, Point size);

    void createPauseButton();

    void soundPool();

    void tryCatch(Context context);

    void callConstructors(Context context, Point size);

    void newGame();

    void update();

    void draw();

    void drawConditions();

    void drawPaused();

    void pause();

    void resume();
    void drawColorSize();
}