package com.github.ghomerl.chimken.model.entities.projectiles;


public class EggProjectile extends Projectile {

    private static final float WIDTH = 56f;
    private static final float HEIGHT = 70f;
    private static final float SPEED = 400f;
    private static final int DAMAGE = 1;

    // Off-screen bounds
    private static final float X_LOWER = -100f;
    private static final float X_UPPER = 2020f;
    private static final float Y_LOWER = -100f;
    private static final float Y_UPPER = 1200f;


    private final float directionX;


    public EggProjectile(float x, float y, float directionX, float directionY) {
        super(x, y, WIDTH, HEIGHT, SPEED, DAMAGE, directionY);
        this.directionX = directionX;
    }


    @Override
    public void update(float delta) {
        setX(getX() + getSpeed() * directionX * delta);
        setY(getY() + getSpeed() * getDirectionY() * delta);

        if (getX() < X_LOWER || getX() > X_UPPER
            || getY() < Y_LOWER || getY() > Y_UPPER) {
            setActive(false);
        }
    }

    public float getDirectionX() {
        return directionX;
    }
}
