package com.github.ghomerl.chimken.model.entities.projectiles;


public class MissileProjectile {

    private static final float SPEED = 800f;

    private float x;
    private float y;
    private final float targetX;
    private final float targetY;

    private boolean active = true;
    private boolean exploded = false;

    public MissileProjectile(float startX, float startY,
                             float targetX, float targetY) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
    }


    public void update(float delta) {
        if (exploded || !active) return;

        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        float step = SPEED * delta;

        if (step >= dist) {
            x = targetX;
            y = targetY;
            exploded = true;
        } else {
            x += (dx / dist) * step;
            y += (dy / dist) * step;
        }
    }


    public float getX() { return x; }
    public float getY() { return y; }
    public float getTargetX() { return targetX; }
    public float getTargetY() { return targetY; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean hasExploded() { return exploded; }
}
