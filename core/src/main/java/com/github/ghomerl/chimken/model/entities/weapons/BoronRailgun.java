package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.SuperheatedMetalloidChunk;

/
public class BoronRailgun extends Weapon {


    private static final float FIRE_RATE = 0.6f;

    public BoronRailgun() {
        super(FIRE_RATE);
    }


    @Override
    public void fire(float centerX, float y, float directionY) {
        if (!canFire()) {
            return;
        }
        float halfW = 7f; // half of SuperheatedMetalloidChunk.WIDTH (14)
        projectiles.add(new SuperheatedMetalloidChunk(centerX - halfW, y, directionY));
        resetCooldown();
    }
}
