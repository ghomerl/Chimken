package com.github.ghomerl.chimken.model.entities.enemies;

import com.github.ghomerl.chimken.model.entities.weapons.DoubleEggThrower;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;


// Chicken enemy that drops two eggs side-by-side straight down.
public class DoubleEggChicken extends Enemy {

    public static final float DEFAULT_WIDTH = 96f;
    public static final float DEFAULT_HEIGHT = 96f;
    private static final float DEFAULT_SPEED = 0f;
    private static final int DEFAULT_HP = 5;
    private static final int DEFAULT_DAMAGE = 1;
    private static final int DEFAULT_POINTS = 200;


    public DoubleEggChicken(float x, float y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SPEED,
            DEFAULT_HP, DEFAULT_DAMAGE, DEFAULT_POINTS, new DoubleEggThrower());
    }


    public DoubleEggChicken(float x, float y, float width, float height,
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
