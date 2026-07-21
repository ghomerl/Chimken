package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.EggProjectile;


public class BossWeapon extends Weapon {

    private static final float EGG_HALF_W = 16f;
    private static final float EGG_HALF_H = 20f;


    private static final float FIRE_RATE = 0f;

    public BossWeapon() {
        super(FIRE_RATE);
    }


    @Override
    public void fire(float centerX, float y, float directionY) {

    }

    public void fireCross(float centerX, float centerY, float rotationRadians) {
        for (int i = 0; i < 4; i++) {
            float angle = rotationRadians + i * (float) Math.PI / 2f;
            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);
            projectiles.add(new EggProjectile(
                centerX - EGG_HALF_W,
                centerY - EGG_HALF_H,
                dx, dy
            ));
        }
    }
}
