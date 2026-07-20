package com.github.ghomerl.chimken.model.entities.items;

public class Burger extends BouncingItem {
    private static final float WIDTH = 30f;
    private static final float HEIGHT = 24f;
    private static final int POINTS = 350;
    private static final int FOOD = 5;

    public Burger(float x, float y, float vx, float vy, float worldWidth) {
        super(x, y, WIDTH, HEIGHT, POINTS, vx, vy, worldWidth);
        this.foodUnits = FOOD;
    }
}
