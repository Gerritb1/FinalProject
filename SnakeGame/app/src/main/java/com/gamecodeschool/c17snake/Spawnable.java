package com.gamecodeschool.c17snake;

import android.graphics.Point;

import java.util.List;

public interface Spawnable {
    Point spawn(List<Point> rockLocations);
}
