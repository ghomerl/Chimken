package com.github.ghomerl.chimken.model.entities.items;

public class PowerUpItem extends FallingItem {
    private static final float WIDTH = 28f;
    private static final float HEIGHT = 28f;
    private static final int POINTS = 1000;

    public PowerUpItem(float x, float y) {
        super(x, y, WIDTH, HEIGHT, POINTS);
    }
}
