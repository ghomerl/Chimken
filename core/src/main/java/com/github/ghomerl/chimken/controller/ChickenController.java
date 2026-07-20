package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.enemies.Enemy;

import java.util.Random;

/**
 * Controls any chicken-type enemy that fires eggs straight down.
 * Each frame there is a {@value #FIRE_CHANCE_DENOMINATOR}-in-1
 * probability of firing.
 */
public class ChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 240;

    private final Enemy enemy;
    private final Random random = new Random();

    /**
     * @param enemy the enemy to control (must have a weapon set)
     */
    public ChickenController(Enemy enemy) {
        this.enemy = enemy;
    }

    /**
     * Rolls the dice and, on a hit, tells the weapon to fire downward.
     * Also ticks the weapon's projectile list.  Dead enemies still
     * have their projectiles updated so in-flight eggs persist.
     */
    public void update(float delta) {
        if (!enemy.isAlive()) {
            if (enemy.getWeapon() != null) {
                enemy.getWeapon().update(delta);
            }
            return;
        }
        if (random.nextInt(FIRE_CHANCE_DENOMINATOR) == 0 && enemy.getWeapon() != null) {
            enemy.getWeapon().fire(
                enemy.getX() + enemy.getWidth() * 0.5f,
                enemy.getY(),
                -1f
            );
        }
        if (enemy.getWeapon() != null) {
            enemy.getWeapon().update(delta);
        }
    }
}
