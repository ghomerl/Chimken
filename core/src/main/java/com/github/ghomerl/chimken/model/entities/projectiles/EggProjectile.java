package com.github.ghomerl.chimken.model.entities.projectiles;

/**
 * An egg projectile that can travel in any 2D direction.
 * Overrides the base {@link #update(float)} to move along both axes,
 * making it suitable for straight-down drops <em>and</em> aimed shots.
 */
public class EggProjectile extends Projectile {

    private static final float WIDTH = 16f;
    private static final float HEIGHT = 20f;
    private static final float SPEED = 300f;
    private static final int DAMAGE = 1;

    /** Off-screen bounds (generous margin beyond the 1920×1080 world). */
    private static final float X_LOWER = -100f;
    private static final float X_UPPER = 2020f;
    private static final float Y_LOWER = -100f;
    private static final float Y_UPPER = 1200f;

    /** Normalised X component of the flight direction. */
    private final float directionX;

    /**
     * @param x          left-edge x position
     * @param y          bottom-edge y position
     * @param directionX normalised horizontal direction (e.g. 0 for straight down)
     * @param directionY normalised vertical direction (e.g. -1 for downward)
     */
    public EggProjectile(float x, float y, float directionX, float directionY) {
        super(x, y, WIDTH, HEIGHT, SPEED, DAMAGE, directionY);
        this.directionX = directionX;
    }

    /**
     * Moves the egg along both axes and deactivates it when it leaves
     * the visible area in <em>either</em> dimension.
     */
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
