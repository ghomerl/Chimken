package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.ChickenEnemy;

import java.util.Random;


public class ChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 240;

    private final ChickenEnemy enemy;
    private final Random random = new Random();

    public ChickenController(ChickenEnemy enemy) {
        this.enemy = enemy;
    }


    public void update(float delta) {
        if (random.nextInt(FIRE_CHANCE_DENOMINATOR) == 0) {
            enemy.getWeapon().fire(
                enemy.getX() + enemy.getWidth() * 0.5f,
                enemy.getY(),
                -1f // straight down
            );
        }
        enemy.getWeapon().update(delta);
    }
}
