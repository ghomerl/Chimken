package com.github.ghomerl.chimken.model.entities;

import com.github.ghomerl.chimken.model.entities.weapons.DoubleEggThrower;


public class DoubleEggChicken extends Enemy {

    public static final float WIDTH = 72f;
    public static final float HEIGHT = 72f;
    private static final float SPEED = 0f;
    private static final int HP = 5;
    private static final int DAMAGE = 1;
    private static final int POINTS = 200;

    public DoubleEggChicken(float x, float y) {
        super(x, y, WIDTH, HEIGHT, SPEED, HP, DAMAGE, POINTS);
        setWeapon(new DoubleEggThrower());
    }
}
