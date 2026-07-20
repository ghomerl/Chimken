package com.github.ghomerl.chimken.model.entities.enemies;

import com.github.ghomerl.chimken.model.entities.weapons.DoubleEggThrower;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;

/**
 * Chicken enemy that drops two eggs side-by-side straight down.
 */
public class DoubleEggChicken extends Enemy {

    public static final float DEFAULT_WIDTH = 72f;
    public static final float DEFAULT_HEIGHT = 72f;
    private static final float DEFAULT_SPEED = 0f;
    private static final int DEFAULT_HP = 5;
    private static final int DEFAULT_DAMAGE = 1;
    private static final int DEFAULT_POINTS = 200;

    /**
     * Creates a double-egg chicken with default stats and a
     * {@link DoubleEggThrower}.
     */
    public DoubleEggChicken(float x, float y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SPEED,
            DEFAULT_HP, DEFAULT_DAMAGE, DEFAULT_POINTS, new DoubleEggThrower());
    }

    /**
     * Fully parameterised constructor for level-design flexibility.
     *
     * @param x       horizontal position
     * @param y       vertical position
     * @param width   hit-box / render width
     * @param height  hit-box / render height
     * @param speed   movement speed (px/s)
     * @param hp      starting and maximum HP
     * @param damage  contact damage to the player
     * @param points  score awarded on kill
     * @param weapon  the weapon this chicken fires (may be {@code null})
     */
    public DoubleEggChicken(float x, float y, float width, float height,
                            float speed, int hp, int damage, int points,
                            Weapon weapon) {
        super(x, y, width, height, speed, hp, damage, points);
        if (weapon != null) {
            setWeapon(weapon);
        }
    }
}
