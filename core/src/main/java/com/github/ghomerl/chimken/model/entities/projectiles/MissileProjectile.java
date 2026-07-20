package com.github.ghomerl.chimken.model.entities.projectiles;

/**
 * A player-fired missile that travels toward the centre of the
 * screen and then detonates, dealing massive area-of-effect damage
 * to every enemy.
 * <p>
 * Does <b>not</b> participate in collision detection — the
 * explosion is handled directly by the game screen.
 */
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

    /**
     * Moves the missile toward the target.  Once it arrives the
     * missile is marked as {@link #hasExploded() exploded} and
     * the caller should trigger the area-of-effect damage.
     */
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

    // ── Accessors ──────────────────────────────────────────────────

    public float getX() { return x; }
    public float getY() { return y; }
    public float getTargetX() { return targetX; }
    public float getTargetY() { return targetY; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean hasExploded() { return exploded; }
}