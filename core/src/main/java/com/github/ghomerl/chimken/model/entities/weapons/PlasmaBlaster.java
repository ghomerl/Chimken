package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.PlasmaLaserProjectile;


public class PlasmaBlaster extends Weapon {


    private static final float FIRE_RATE = 0.25f;

    public PlasmaBlaster() {
        super(FIRE_RATE);
    }


    @Override
    public void fire(float centerX, float y, float directionY) {
        if (!canFire()) {
            return;
        }
        float halfW = 4f;
        projectiles.add(new PlasmaLaserProjectile(centerX - halfW, y, directionY));
        resetCooldown();
    }
}
