package com.github.ghomerl.chimken.model.entities.enemies;

import com.github.ghomerl.chimken.model.entities.weapons.EggThrower;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;

/**
 * Basic chicken enemy that drops a single egg straight down.
 */
public class ChickenEnemy extends Enemy {

    public static final float DEFAULT_WIDTH = 64f;
    public static final float DEFAULT_HEIGHT = 64f;
    private static final float DEFAULT_SPEED = 0f;
    private static final int DEFAULT_HP = 3;
    private static final int DEFAULT_DAMAGE = 1;
    private static final int DEFAULT_POINTS = 100;

    /**
     * Creates a chicken with default stats and an {@link EggThrower}.
     */
    public ChickenEnemy(float x, float y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SPEED,
            DEFAULT_HP, DEFAULT_DAMAGE, DEFAULT_POINTS, new EggThrower());
    }

    /**
     * Fully parameterised constructor for level-design flexibility.
     * Every stat can be tuned per wave.
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
    public ChickenEnemy(float x, float y, float width, float height,
                        float speed, int hp, int damage, int points,
                        Weapon weapon) {
        super(x, y, width, height, speed, hp, damage, points);
        if (weapon != null) {
            setWeapon(weapon);
        }
    }
}
