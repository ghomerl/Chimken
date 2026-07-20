package com.github.ghomerl.chimken.model.entities.items;

public class KeyItem extends BouncingItem {
    private static final float WIDTH = 18f;
    private static final float HEIGHT = 24f;
    private static final int POINTS = 0;

    public KeyItem(float x, float y, float vx, float vy, float worldWidth) {
        super(x, y, WIDTH, HEIGHT, POINTS, vx, vy, worldWidth);
        this.keyCount = 1;
    }
}
