package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.EggProjectile;

/**
 * Fires a single {@link EggProjectile} aimed at a specific target position.
 * The egg travels in a straight line at the calculated angle and does
 * <b>not</b> change direction mid-flight.
 * Used by the sniper chicken enemy.
 */
public class SniperEggThrower extends Weapon {

    private static final float FIRE_RATE = 0.1f;
    private static final float EGG_HALF_W = 8f;

    public SniperEggThrower() {
        super(FIRE_RATE);
    }

    @Override
    public void fire(float centerX, float y, float directionY) {
        if (!canFire()) {
            return;
        }
        // Fallback: fire straight down
        projectiles.add(new EggProjectile(centerX - EGG_HALF_W, y, 0f, directionY));
        resetCooldown();
    }

    /**
     * Fires an egg from ({@code fromX}, {@code fromY}) toward
     * ({@code targetX}, {@code targetY}).
     * The direction vector is normalised so the egg always travels
     * at its configured speed regardless of distance.
     */
    @Override
    public void fireAt(float fromX, float fromY, float targetX, float targetY) {
        if (!canFire()) {
            return;
        }
        float dx = targetX - fromX;
        float dy = targetY - fromY;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0f) {
            return; // avoid division by zero
        }
        float ndx = dx / len;
        float ndy = dy / len;
        projectiles.add(new EggProjectile(fromX - EGG_HALF_W, fromY, ndx, ndy));
        resetCooldown();
    }
}
