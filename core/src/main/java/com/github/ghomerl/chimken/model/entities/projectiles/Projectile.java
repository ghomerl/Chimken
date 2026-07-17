package com.github.ghomerl.chimken.model.entities.projectiles;

import com.badlogic.gdx.math.Rectangle;

/**
 * Base projectile model.
 * Projectiles move across the screen in a given vertical direction
 * and carry a damage value that is applied on impact.
 * Subclasses define specific projectile types with their own
 * dimensions, speed, and damage.
 */
public class Projectile {

    /** Y-coordinates beyond which the projectile is considered off-screen. */
    private static final float LOWER_BOUND = -100f;
    private static final float UPPER_BOUND = 1200f;

    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;
    private int damage;
    private float directionY;   // positive = upward, negative = downward
    private boolean active;

    public Projectile(float x, float y, float width, float height,
                      float speed, int damage, float directionY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.damage = damage;
        this.directionY = directionY;
        this.active = true;
    }

    /**
     * Moves the projectile along its trajectory and deactivates it
     * when it leaves the visible area.
     */
    public void update(float delta) {
        y += speed * directionY * delta;
        if (y < LOWER_BOUND || y > UPPER_BOUND) {
            active = false;
        }
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    // ── Accessors ──────────────────────────────────────────────────

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getDirectionY() {
        return directionY;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
