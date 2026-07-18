package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.PlasmaLaserProjectile;

/**
 * Rapid-fire plasma blaster — the default weapon equipped by the player.
 * Fires {@link PlasmaLaserProjectile} instances at a steady rate.
 */
public class PlasmaBlaster extends Weapon {

    /** Ten shots per second. */
    private static final float FIRE_RATE = 0.1f;

    public PlasmaBlaster() {
        super(FIRE_RATE);
    }

    /**
     * Spawns a {@link PlasmaLaserProjectile} centred on {@code centerX}
     * if the weapon is off cooldown.
     */
    @Override
    public void fire(float centerX, float y, float directionY) {
        if (!canFire()) {
            return;
        }
        float halfW = 4f; // half of PlasmaLaserProjectile.WIDTH (8)
        projectiles.add(new PlasmaLaserProjectile(centerX - halfW, y, directionY));
        resetCooldown();
    }
}
