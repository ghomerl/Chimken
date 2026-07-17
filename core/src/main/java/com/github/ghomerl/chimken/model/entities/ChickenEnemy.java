package com.github.ghomerl.chimken.model.entities;

import com.github.ghomerl.chimken.model.entities.weapons.EggThrower;


public class ChickenEnemy extends Enemy {

    public static final float WIDTH = 64f;
    public static final float HEIGHT = 64f;
    private static final float SPEED = 0f;
    private static final int HP = 3;
    private static final int DAMAGE = 1;
    private static final int POINTS = 100;

    public ChickenEnemy(float x, float y) {
        super(x, y, WIDTH, HEIGHT, SPEED, HP, DAMAGE, POINTS);
        setWeapon(new EggThrower());
    }
}
