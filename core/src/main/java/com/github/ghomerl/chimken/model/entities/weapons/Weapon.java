package com.github.ghomerl.chimken.model.entities.weapons;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;


public abstract class Weapon {

    protected float fireRate;
    protected float cooldownTimer;
    protected final Array<Projectile> projectiles;

    public Weapon(float fireRate) {
        this.fireRate = fireRate;
        this.cooldownTimer = 0f;
        this.projectiles = new Array<>();
    }


    public abstract void fire(float centerX, float y, float directionY);


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
