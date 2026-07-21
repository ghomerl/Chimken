package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.PlasmaLaserProjectile;
import com.github.ghomerl.chimken.model.entities.weapons.enums.WeaponType;

/**
 * Rapid-fire plasma blaster — the default weapon equipped by the player.
 * Fires {@link PlasmaLaserProjectile} instances at a steady rate.
 */
public class PlasmaBlaster extends Weapon {

    /** Four shots per second. */
    private static final float FIRE_RATE = 0.25f;
    private static final float BASE_DAMAGE = 1f;
    private static final float SPREAD = 20f;

    public PlasmaBlaster() {
        super(FIRE_RATE);
    }

    @Override
    public WeaponType getWeaponType() {
        return WeaponType.PLASMA_BLASTER;
    }

    @Override
    public float getProjectileSpeed() {
        return 600f;
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
        float halfW = 4f;
        projectiles.add(new PlasmaLaserProjectile(centerX - halfW, y, directionY));
        resetCooldown();
    }

    @Override
    protected void spawnLeveledProjectiles(float centerX, float y, float directionY,
                                           int extraProjectiles, float damageMultiplier) {
        int damage = Math.max(1, Math.round(BASE_DAMAGE * damageMultiplier));
        float halfW = 4f;
        int total = 1 + extraProjectiles;
        float startX = centerX - (total - 1) * SPREAD / 2f;
        for (int i = 0; i < total; i++) {
            projectiles.add(new PlasmaLaserProjectile(
                startX + i * SPREAD - halfW, y, directionY, damage));
        }
    }
}
