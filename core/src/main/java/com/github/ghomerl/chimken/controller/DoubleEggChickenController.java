package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.enemies.DoubleEggChicken;

import java.util.Random;


public class DoubleEggChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 360;

    private final DoubleEggChicken enemy;
    private final Random random = new Random();

    public DoubleEggChickenController(DoubleEggChicken enemy) {
        this.enemy = enemy;
    }


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
