package com.github.ghomerl.chimken.model.entities.weapons;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.model.entities.weapons.enums.WeaponType;


public abstract class Weapon {


    private static final int[] EXTRA_PROJECTILE_LEVELS = {3, 5, 10, 20};


    protected float fireRate;
    protected float cooldownTimer;
    protected final Array<Projectile> projectiles;

    public Weapon(float fireRate) {
        this.fireRate = fireRate;
        this.cooldownTimer = 0f;
        this.projectiles = new Array<>();
    }


    public WeaponType getWeaponType() {
        return null; // override in player weapon subclasses
    }


    public abstract void fire(float centerX, float y, float directionY);


    public void fireLeveled(float centerX, float y, float directionY, int level) {
        if (!canFire()) {
            return;
        }
        int extras = countExtraProjectiles(level);
        float dmgMult = calculateDamageMultiplier(level);
        spawnLeveledProjectiles(centerX, y, directionY, extras, dmgMult);
        resetCooldown();
    }


    protected void spawnLeveledProjectiles(float centerX, float y, float directionY,
                                           int extraProjectiles, float damageMultiplier) {
        // Only player weapons need to override this.
    }


    public void fireAt(float fromX, float fromY, float targetX, float targetY) {
        fire(fromX, fromY, -1f);
    }


    public float getProjectileSpeed() {
        return 0f;
    }



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

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    public void dispose() {
        projectiles.clear();
    }

    protected boolean canFire() {
        return cooldownTimer <= 0f;
    }

    protected void resetCooldown() {
        cooldownTimer = fireRate;
    }
}
