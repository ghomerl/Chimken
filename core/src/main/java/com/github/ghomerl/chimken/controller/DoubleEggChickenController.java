package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.DoubleEggChicken;

import java.util.Random;

/**
 * Controls a {@link DoubleEggChicken}: each frame there is a
 * {@value #FIRE_CHANCE_DENOMINATOR}-in-1 probability of firing
 * two eggs side-by-side straight down.
 */
public class DoubleEggChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 246;

    private final DoubleEggChicken enemy;
    private final Random random = new Random();

    public DoubleEggChickenController(DoubleEggChicken enemy) {
        this.enemy = enemy;
    }

    /**
     * Rolls the dice and, on a hit, tells the weapon to fire a double
     * volley downward.  Also ticks the weapon's projectile list.
     */
    public void update(float delta) {
        if (!enemy.isAlive()) {
            enemy.getWeapon().update(delta);
            return;
        }
        if (random.nextInt(FIRE_CHANCE_DENOMINATOR) == 0) {
            enemy.getWeapon().fire(
                enemy.getX() + enemy.getWidth() * 0.5f,
                enemy.getY(),
                -1f
            );
        }
        enemy.getWeapon().update(delta);
    }
}
