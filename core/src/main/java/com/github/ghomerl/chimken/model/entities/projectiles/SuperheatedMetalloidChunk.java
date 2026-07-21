package com.github.ghomerl.chimken.model.entities.projectiles;


public class SuperheatedMetalloidChunk extends Projectile {

    private static final float WIDTH = 28f;
    private static final float HEIGHT = 56f;
    private static final float SPEED = 900f;
    private static final int DAMAGE = 3;


    public SuperheatedMetalloidChunk(float x, float y, float directionY) {
        super(x, y, WIDTH, HEIGHT, SPEED, DAMAGE, directionY);
    }


    public SuperheatedMetalloidChunk(float x, float y, float directionY, int damage) {
        super(x, y, WIDTH, HEIGHT, SPEED, damage, directionY);
    }
}
