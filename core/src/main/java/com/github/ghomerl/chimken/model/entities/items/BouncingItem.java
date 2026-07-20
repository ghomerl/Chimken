package com.github.ghomerl.chimken.model.entities.items;

public abstract class BouncingItem extends Item {
    private static final float GRAVITY = 1500f;
    private static final int MAX_FRAMES = 360;

    protected float vx, vy;
    protected float worldWidth;
    private int framesAlive;

    public BouncingItem(float x, float y, float width, float height, int points,
                        float vx, float vy, float worldWidth) {
        super(x, y, width, height, points);
        this.vx = vx;
        this.vy = vy;
        this.worldWidth = worldWidth;
        this.framesAlive = 0;
    }

    @Override
    public void update(float delta) {
        vy -= GRAVITY * delta;
        x += vx * delta;
        y += vy * delta;

        // Ground bounce (y = 0)
        if (y < 0f) {
            y = 0f;
            vy = Math.abs(vy);
        }
        // Left wall
        if (x < 0f) {
            x = 0f;
            vx = Math.abs(vx);
        }
        // Right wall
        if (x + width > worldWidth) {
            x = worldWidth - width;
            vx = -Math.abs(vx);
        }

        framesAlive++;
        if (framesAlive >= MAX_FRAMES) active = false;
    }

    public float getVx() { return vx; }
    public float getVy() { return vy; }
}
