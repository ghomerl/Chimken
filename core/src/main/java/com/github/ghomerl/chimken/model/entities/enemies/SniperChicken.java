package com.github.ghomerl.chimken.model.entities.enemies;

import com.github.ghomerl.chimken.model.entities.weapons.SniperEggThrower;


public class SniperChicken extends Enemy {

    public static final float WIDTH = 56f;
    public static final float HEIGHT = 56f;
    private static final float SPEED = 0f;
    private static final int HP = 4;
    private static final int DAMAGE = 1;
    private static final int POINTS = 300;

    public SniperChicken(float x, float y) {
        super(x, y, WIDTH, HEIGHT, SPEED, HP, DAMAGE, POINTS);
        setWeapon(new SniperEggThrower());
    }
}
