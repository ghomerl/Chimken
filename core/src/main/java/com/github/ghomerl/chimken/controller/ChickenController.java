package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.enemies.Enemy;

import java.util.Random;


public class ChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 360;

    private final Enemy enemy;
    private final Random random = new Random();


    public ChickenController(Enemy enemy) {
        this.enemy = enemy;
    }


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
