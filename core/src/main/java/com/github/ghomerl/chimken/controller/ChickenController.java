package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.enemies.ChickenEnemy;

import java.util.Random;

/**
 * Controls a {@link ChickenEnemy}: each frame there is a
 * {@value #FIRE_CHANCE_DENOMINATOR}-in-1 probability of firing
 * a single egg straight down.
 */
public class ChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 240;

    private final ChickenEnemy enemy;
    private final Random random = new Random();

    public ChickenController(ChickenEnemy enemy) {
        this.enemy = enemy;
    }

    /**
     * Rolls the dice and, on a hit, tells the weapon to fire downward.
     * Also ticks the weapon's projectile list.
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
