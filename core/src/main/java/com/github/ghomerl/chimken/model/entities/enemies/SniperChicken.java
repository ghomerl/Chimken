package com.github.ghomerl.chimken.model.entities.enemies;

import com.github.ghomerl.chimken.model.entities.weapons.SniperEggThrower;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;

/**
 * Chicken enemy that fires a single egg aimed at the player.
 * The egg travels in a straight line and does not track the player
 * after being fired.
 */
public class SniperChicken extends Enemy {

    public static final float DEFAULT_WIDTH = 56f;
    public static final float DEFAULT_HEIGHT = 56f;
    private static final float DEFAULT_SPEED = 0f;
    private static final int DEFAULT_HP = 4;
    private static final int DEFAULT_DAMAGE = 1;
    private static final int DEFAULT_POINTS = 300;

    /**
     * Creates a sniper chicken with default stats and a
     * {@link SniperEggThrower}.
     */
    public SniperChicken(float x, float y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SPEED,
            DEFAULT_HP, DEFAULT_DAMAGE, DEFAULT_POINTS, new SniperEggThrower());
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
    public SniperChicken(float x, float y, float width, float height,
                         float speed, int hp, int damage, int points,
                         Weapon weapon) {
        super(x, y, width, height, speed, hp, damage, points);
        if (weapon != null) {
            setWeapon(weapon);
        }
    }
}
