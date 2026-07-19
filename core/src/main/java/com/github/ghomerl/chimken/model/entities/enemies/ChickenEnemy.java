package com.github.ghomerl.chimken.model.entities.enemies;

import com.github.ghomerl.chimken.model.entities.weapons.EggThrower;

/**
 * Basic chicken enemy that drops a single egg straight down.
 */
public class ChickenEnemy extends Enemy {

    public static final float WIDTH = 64f;
    public static final float HEIGHT = 64f;
    private static final float SPEED = 0f;   // stationary for now
    private static final int HP = 3;
    private static final int DAMAGE = 1;
    private static final int POINTS = 100;

    public ChickenEnemy(float x, float y) {
        super(x, y, WIDTH, HEIGHT, SPEED, HP, DAMAGE, POINTS);
        setWeapon(new EggThrower());
    }
}
