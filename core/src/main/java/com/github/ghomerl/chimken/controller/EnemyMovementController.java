package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.math.MathUtils;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.SpawnEntry;

/**
 * Moves enemies from their spawn position towards their target
 * position (defined in {@link SpawnEntry}) and stops them once
 * they arrive.
 * <p>
 * Each enemy is tracked by a simple "reached target" flag.
 * Once an enemy reaches its target, {@link Enemy#setSpeed(float)}
 * is set to 0 and it will no longer move.
 */
public final class EnemyMovementController {

    private EnemyMovementController() {
    }

    /**
     * Moves all provided enemies towards their targets.
     * Enemies that have arrived are stopped and skipped on
     * subsequent frames.
     *
     * @param spawns the spawn entries (each holds an enemy + target)
     * @param delta  frame delta in seconds
     */
    public static void update(com.badlogic.gdx.utils.Array<com.github.ghomerl.chimken.model.entities.enemies.SpawnEntry> spawns,
                              float delta) {
        for (com.github.ghomerl.chimken.model.entities.enemies.SpawnEntry entry : spawns) {
            Enemy e = entry.enemy;
            if (!e.isAlive()) continue;
            if (e.getSpeed() <= 0f) continue; // already stopped

            float dx = entry.targetX - e.getX();
            float dy = entry.targetY - e.getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            float step = e.getSpeed() * delta;

            if (step >= dist) {
                // Snap to target
                e.setX(entry.targetX);
                e.setY(entry.targetY);
                e.setSpeed(0f);
            } else {
                // Move towards target
                e.setX(e.getX() + (dx / dist) * step);
                e.setY(e.getY() + (dy / dist) * step);
            }
        }
    }
}
