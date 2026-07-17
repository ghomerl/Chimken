package com.github.ghomerl.chimken.model.entities.weapons;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;

/**
 * Base weapon class.
 * A weapon lives inside a {@link com.github.ghomerl.chimken.model.entities.Player}
 * or {@link com.github.ghomerl.chimken.model.entities.Enemy} and is responsible
 * for managing its own projectile instances — creating them on fire,
 * updating their positions every frame, and removing inactive ones.
 */
public abstract class Weapon {

    /** Seconds between consecutive shots. */
    protected float fireRate;
    protected float cooldownTimer;
    protected final Array<Projectile> projectiles;

    public Weapon(float fireRate) {
        this.fireRate = fireRate;
        this.cooldownTimer = 0f;
        this.projectiles = new Array<>();
    }

    /**
     * Attempts to fire a projectile from the given position.
     * The call is silently ignored while the weapon is on cooldown.
     *
     * @param centerX   the horizontal centre of the entity firing
     * @param y         the vertical spawn position (usually top/bottom of the entity)
     * @param directionY positive = upward, negative = downward
     */
    public abstract void fire(float centerX, float y, float directionY);

    /**
     * Fires a projectile aimed at a specific world position.
     * The default implementation simply fires straight down;
     * subclasses that support aimed fire should override this.
     *
     * @param fromX   x of the firing origin
     * @param fromY   y of the firing origin
     * @param targetX x of the target
     * @param targetY y of the target
     */
    public void fireAt(float fromX, float fromY, float targetX, float targetY) {
        fire(fromX, fromY, -1f);
    }

    /**
     * Ticks the cooldown timer and updates every active projectile.
     * Projectiles that have left the screen or been deactivated are
     * removed from the internal list.
     */
    public void update(float delta) {
        if (cooldownTimer > 0f) {
            cooldownTimer -= delta;
        }
        for (int i = projectiles.size - 1; i >= 0; i--) {
            projectiles.get(i).update(delta);
            if (!projectiles.get(i).isActive()) {
                projectiles.removeIndex(i);
            }
        }
    }

    // ── Accessors ──────────────────────────────────────────────────

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    // ── Lifecycle ──────────────────────────────────────────────────

    public void dispose() {
        projectiles.clear();
    }

    // ── Protected helpers ──────────────────────────────────────────

    protected boolean canFire() {
        return cooldownTimer <= 0f;
    }

    protected void resetCooldown() {
        cooldownTimer = fireRate;
    }
}
