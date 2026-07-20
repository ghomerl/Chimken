package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.projectiles.SuperheatedMetalloidChunk;


public class BoronRailgun extends Weapon {

    private static final float FIRE_RATE = 0.6f;
    private static final float BASE_DAMAGE = 3f;
    private static final float SPREAD = 24f;

    public BoronRailgun() {
        super(FIRE_RATE);
    }

    @Override
    public float getProjectileSpeed() {
        return 900f;
    }

    @Override
    public void fire(float centerX, float y, float directionY) {
        if (!canFire()) {
            return;
        }
        float halfW = 7f;
        projectiles.add(new SuperheatedMetalloidChunk(centerX - halfW, y, directionY));
        resetCooldown();
    }

    @Override
    protected void spawnLeveledProjectiles(float centerX, float y, float directionY,
                                           int extraProjectiles, float damageMultiplier) {
        int damage = Math.max(1, Math.round(BASE_DAMAGE * damageMultiplier));
        float halfW = 7f;
        int total = 1 + extraProjectiles;
        float startX = centerX - (total - 1) * SPREAD / 2f;
        for (int i = 0; i < total; i++) {
            projectiles.add(new SuperheatedMetalloidChunk(
                startX + i * SPREAD - halfW, y, directionY, damage));
        }
    }
}
