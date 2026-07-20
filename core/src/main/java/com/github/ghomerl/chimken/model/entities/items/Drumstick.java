package com.github.ghomerl.chimken.model.entities.items;

public class Drumstick extends BouncingItem {
    private static final float WIDTH = 24f;
    private static final float HEIGHT = 20f;
    private static final int POINTS = 150;
    private static final int FOOD = 1;

    public Drumstick(float x, float y, float vx, float vy, float worldWidth) {
        super(x, y, WIDTH, HEIGHT, POINTS, vx, vy, worldWidth);
        this.foodUnits = FOOD;
    }
}
