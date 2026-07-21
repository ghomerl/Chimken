package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.enemies.SniperChicken;

import java.util.Random;


public class SniperChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 240;

    private final SniperChicken enemy;
    private final Player player;
    private final Random random = new Random();

    public SniperChickenController(SniperChicken enemy, Player player) {
        this.enemy = enemy;
        this.player = player;
    }


    public void update(float delta) {
        if (!enemy.isAlive()) {
            enemy.getWeapon().update(delta);
            return;
        }
        if (random.nextInt(FIRE_CHANCE_DENOMINATOR) == 0) {
            float fromX = enemy.getX() + enemy.getWidth() * 0.5f;
            float fromY = enemy.getY();
            float toX = player.getX() + player.getWidth() * 0.5f;
            float toY = player.getY() + player.getHeight() * 0.5f;
            enemy.getWeapon().fireAt(fromX, fromY, toX, toY);
        }
        enemy.getWeapon().update(delta);
    }
}
