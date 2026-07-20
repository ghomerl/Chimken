package com.github.ghomerl.chimken.model.entities.items;

public abstract class FallingItem extends Item {
    private static final float FALL_SPEED = 150f;
    private static final float LOWER_BOUND = -100f;

    public FallingItem(float x, float y, float width, float height, int points) {
        super(x, y, width, height, points);
    }

    @Override
    public void update(float delta) {
        y -= FALL_SPEED * delta;
        if (y < LOWER_BOUND) active = false;
    }
}
