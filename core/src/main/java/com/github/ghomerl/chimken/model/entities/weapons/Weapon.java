package com.github.ghomerl.chimken.model.entities.weapons;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.model.entities.weapons.enums.WeaponType;

/**
 * Base weapon class.
 * A weapon lives inside a {@link com.github.ghomerl.chimken.model.entities.Player}
 * or {@link com.github.ghomerl.chimken.model.entities.enemies.Enemy} and is responsible
 * for managing its own projectile instances — creating them on fire,
 * updating their positions every frame, and removing inactive ones.
 */
public abstract class Weapon {

    /** Levels at which an extra projectile is added instead of a damage × 1.1. */
    private static final int[] EXTRA_PROJECTILE_LEVELS = {3, 5, 10, 20};

    /** Seconds between consecutive shots. */
    protected float fireRate;
    protected float cooldownTimer;
    protected final Array<Projectile> projectiles;

    public Weapon(float fireRate) {
        this.fireRate = fireRate;
        this.cooldownTimer = 0f;
        this.projectiles = new Array<>();
    }

    // ── Firing ────────────────────────────────────────────────────

    /**
     * Returns the {@link WeaponType} that this weapon instance
     * represents.  Used by the gift-collection logic to detect
     * whether a duplicate weapon should level up instead of replace.
     * <p>
     * Enemy weapons (e.g. EggThrower) return {@code null} because
     * they are never compared with gifts.
     */
    public WeaponType getWeaponType() {
        return null; // override in player weapon subclasses
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
     * Level-aware variant of {@link #fire}.  Used by the player.
     * <p>
     * At levels 3, 5, 10, 20 an extra side-by-side projectile is added
     * (instead of the normal damage × 1.1 for that level).  Every other
     * level multiplies base damage by 1.1.
     * <p>
     * Subclasses that are player weapons should override
     * {@link #spawnLeveledProjectiles} to support this.  Enemy weapons
     * are always fired via {@link #fire} so the default no-op is fine.
     *
     * @param centerX    horizontal centre of the entity
     * @param y          vertical spawn position
     * @param directionY positive = up, negative = down
     * @param level      the player's current weapon level (≥ 1)
     */
    public void fireLeveled(float centerX, float y, float directionY, int level) {
        if (!canFire()) {
            return;
        }
        int extras = countExtraProjectiles(level);
        float dmgMult = calculateDamageMultiplier(level);
        spawnLeveledProjectiles(centerX, y, directionY, extras, dmgMult);
        resetCooldown();
    }

    /**
     * Called by {@link #fireLeveled} to create the actual projectiles.
     * Default implementation does nothing (enemy weapons are never
     * fired through this path).  Override in player weapons.
     *
     * @param centerX         horizontal centre
     * @param y               vertical spawn position
     * @param directionY      up / down
     * @param extraProjectiles how many additional projectiles to spawn
     *                          (0 at level 1, 1 at level 3, etc.)
     * @param damageMultiplier multiplier applied to base damage
     */
    protected void spawnLeveledProjectiles(float centerX, float y, float directionY,
                                           int extraProjectiles, float damageMultiplier) {
        // Only player weapons need to override this.
    }

    /**
     * Fires a projectile aimed at a specific world position.
     * The default implementation simply fires straight down;
     * subclasses that support aimed fire should override this.
     */
    public void fireAt(float fromX, float fromY, float targetX, float targetY) {
        fire(fromX, fromY, -1f);
    }

    /**
     * Returns the base projectile speed for this weapon.
     * Used by the drop system to calculate throw velocity for
     * bouncing items.
     */
    public float getProjectileSpeed() {
        return 0f;
    }

    // ── Level math ───────────────────────────────────────────────

    private int countExtraProjectiles(int level) {
        int count = 0;
        for (int lvl : EXTRA_PROJECTILE_LEVELS) {
            if (level >= lvl) count++;
        }
        return count;
    }

    private float calculateDamageMultiplier(int level) {
        int extras = countExtraProjectiles(level);
        int damageLevels = Math.max(0, (level - 1) - extras);
        return (float) Math.pow(1.1, damageLevels);
    }

    // ── Update ────────────────────────────────────────────────────

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
