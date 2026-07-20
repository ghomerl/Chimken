package com.github.ghomerl.chimken.model.entities.items;

public class Roast extends BouncingItem {
    private static final float WIDTH = 36f;
    private static final float HEIGHT = 28f;
    private static final int POINTS = 750;
    private static final int FOOD = 10;

    public Roast(float x, float y, float vx, float vy, float worldWidth) {
        super(x, y, WIDTH, HEIGHT, POINTS, vx, vy, worldWidth);
        this.foodUnits = FOOD;
    }
}
