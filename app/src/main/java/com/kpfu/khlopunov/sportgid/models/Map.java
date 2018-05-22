package com.kpfu.khlopunov.sportgid.models;

import java.io.Serializable;

/**
 * Created by hlopu on 15.05.2018.
 */

public class Map implements Serializable {
    private double x;
    private double y;

    public Map(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
