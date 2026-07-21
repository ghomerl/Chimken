package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;


public final class ZigzagMovementController {


    private static final float FALL_SPEED = 110f;


    private static final float SWAY_AMPLITUDE = 70f;


    private static final float SWAY_FREQUENCY = 1.6f;


    private static final float PHASE_PER_ENEMY = 0.35f;

    private final Array<Enemy> enemies;
    private final float[] baseXs;
    private final float[] baseYs;
    private float elapsed;


    public ZigzagMovementController(Array<Enemy> enemies) {
        this.enemies = enemies;
        this.baseXs = new float[enemies.size];
        this.baseYs = new float[enemies.size];
        for (int i = 0; i < enemies.size; i++) {
            Enemy e = enemies.get(i);
            baseXs[i] = e.getX();
            baseYs[i] = e.getY();
        }
        this.elapsed = 0f;
    }


    public void update(float delta) {
        elapsed += delta;

        for (int i = 0; i < enemies.size; i++) {
            Enemy e = enemies.get(i);
            if (!e.isAlive()) continue;

            float sway = SWAY_AMPLITUDE
                * (float) Math.sin(elapsed * SWAY_FREQUENCY + i * PHASE_PER_ENEMY);
            e.setX(baseXs[i] + sway);
            e.setY(baseYs[i] - FALL_SPEED * elapsed);

            // Mark chickens that fell off the bottom as dead
            if (e.getY() < -e.getHeight() * 2f) {
                e.setAlive(false);
            }
        }
    }
}
