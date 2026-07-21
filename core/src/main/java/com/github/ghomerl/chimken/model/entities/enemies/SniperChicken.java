package com.github.ghomerl.chimken.model.entities.enemies;

import com.github.ghomerl.chimken.model.entities.weapons.SniperEggThrower;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;

/*
    Chicken enemy that fires a single egg aimed at the player.
    The egg travels in a straight line and does not track the player.
*/
public class SniperChicken extends Enemy {

    public static final float DEFAULT_WIDTH = 64f;
    public static final float DEFAULT_HEIGHT = 64f;
    private static final float DEFAULT_SPEED = 0f;
    private static final int DEFAULT_HP = 4;
    private static final int DEFAULT_DAMAGE = 1;
    private static final int DEFAULT_POINTS = 300;


    public SniperChicken(float x, float y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SPEED,
            DEFAULT_HP, DEFAULT_DAMAGE, DEFAULT_POINTS, new SniperEggThrower());
    }

    public SniperChicken(float x, float y, float width, float height,
                         float speed, int hp, int damage, int points,
                         Weapon weapon) {
        super(x, y, width, height, speed, hp, damage, points);
        if (weapon != null) {
            setWeapon(weapon);
        }
    }

    public static int getDefaultHp() {
        return DEFAULT_HP;
    }
}
