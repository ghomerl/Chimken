package com.github.ghomerl.chimken.model.entities.projectiles;


public class PlasmaLaserProjectile extends Projectile {

    private static final float WIDTH = 8f;
    private static final float HEIGHT = 24f;
    private static final float SPEED = 600f;
    private static final int DAMAGE = 1;


    public PlasmaLaserProjectile(float x, float y, float directionY) {
        super(x, y, WIDTH, HEIGHT, SPEED, DAMAGE, directionY);
    }
}
