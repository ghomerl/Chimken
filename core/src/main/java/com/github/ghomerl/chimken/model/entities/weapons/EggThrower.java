package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.EggProjectile;


public class EggThrower extends Weapon {


    private static final float FIRE_RATE = 0.1f;
    private static final float EGG_HALF_W = 8f; // half of EggProjectile.WIDTH (16)

    public EggThrower() {
        super(FIRE_RATE);
    }

    @Override
    public void fire(float centerX, float y, float directionY) {
        if (!canFire()) {
            return;
        }
        projectiles.add(new EggProjectile(centerX - EGG_HALF_W, y, 0f, directionY));
        resetCooldown();
    }
}
