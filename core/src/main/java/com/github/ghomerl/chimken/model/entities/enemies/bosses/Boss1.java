package com.github.ghomerl.chimken.model.entities.enemies.bosses;

import com.github.ghomerl.chimken.model.entities.enemies.enums.BossType;
import com.github.ghomerl.chimken.model.entities.weapons.BossWeapon;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;

public class Boss1 extends Boss {

    public static final float DEFAULT_WIDTH = 300f;
    public static final float DEFAULT_HEIGHT = 300f;
    public static final float DEFAULT_SPEED = 200f;
    public static final int DEFAULT_HP = 3000;
    public static final int DEFAULT_DAMAGE = 1;
    public static final int DEFAULT_POINTS = 10_000;


    public Boss1(float x, float y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SPEED,
            DEFAULT_HP, DEFAULT_DAMAGE, DEFAULT_POINTS, new BossWeapon());
    }


    public Boss1(float x, float y, float width, float height,
                 float speed, int hp, int damage, int points,
                 Weapon weapon) {
        super(BossType.BOSS_1, x, y, width, height, speed, hp, damage, points);
        if (weapon != null) {
            setWeapon(weapon);
        }
    }
}
