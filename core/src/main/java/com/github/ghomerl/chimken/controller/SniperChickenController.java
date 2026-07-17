package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.SniperChicken;

import java.util.Random;

/**
 * Controls a {@link SniperChicken}: each frame there is a
 * {@value #FIRE_CHANCE_DENOMINATOR}-in-1 probability of firing
 * an egg aimed at the player's current position.
 * <p>
 * The egg does <b>not</b> track the player after being fired —
 * it travels in a fixed straight line determined at launch time.
 */
public class SniperChickenController {

    private static final int FIRE_CHANCE_DENOMINATOR = 180;

    private final SniperChicken enemy;
    private final Player player;
    private final Random random = new Random();

    public SniperChickenController(SniperChicken enemy, Player player) {
        this.enemy = enemy;
        this.player = player;
    }

    /**
     * Rolls the dice and, on a hit, fires a targeted egg toward
     * the player's centre.  Also ticks the weapon's projectile list.
     */
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
