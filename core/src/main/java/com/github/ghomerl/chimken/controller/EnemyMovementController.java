package com.github.ghomerl.chimken.controller;


import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.levels.SpawnEntry;


public final class EnemyMovementController {

    private EnemyMovementController() {
    }


    public static void update(com.badlogic.gdx.utils.Array<SpawnEntry> spawns,
                              float delta) {
        for (SpawnEntry entry : spawns) {
            Enemy e = entry.enemy();
            if (!e.isAlive()) continue;
            if (e.getSpeed() <= 0f) continue; // already stopped

            float dx = entry.targetX() - e.getX();
            float dy = entry.targetY() - e.getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            float step = e.getSpeed() * delta;

            if (step >= dist) {
                // Snap to target
                e.setX(entry.targetX());
                e.setY(entry.targetY());
                e.setSpeed(0f);
            } else {
                // Move towards target
                e.setX(e.getX() + (dx / dist) * step);
                e.setY(e.getY() + (dy / dist) * step);
            }
        }
    }
}
