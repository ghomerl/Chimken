package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.EggProjectile;


public class DoubleEggThrower extends Weapon {

    private static final float FIRE_RATE = 0.1f;
    private static final float EGG_HALF_W = 8f;
    private static final float SPREAD = 20f; // horizontal gap between the two eggs

    public DoubleEggThrower() {
        super(FIRE_RATE);
    }

    @Override
    public void fire(float centerX, float y, float directionY) {
        if (!canFire()) {
            return;
        }
        // Left egg
        projectiles.add(new EggProjectile(
            centerX - SPREAD - EGG_HALF_W, y, 0f, directionY));
        // Right egg
        projectiles.add(new EggProjectile(
            centerX + SPREAD - EGG_HALF_W, y, 0f, directionY));
        resetCooldown();
    }
}
