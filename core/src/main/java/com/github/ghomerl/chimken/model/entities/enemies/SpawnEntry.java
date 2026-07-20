package com.github.ghomerl.chimken.model.entities.enemies;

/**
 * Describes one enemy to be spawned as part of a wave.
 * <p>
 * The {@code targetX}/{@code targetY} fields define the position the
 * enemy will move towards and stop at once it arrives.
 * If the target equals the spawn position, the enemy is stationary.
 */
public class SpawnEntry {

    public final Enemy enemy;
    public final float targetX;
    public final float targetY;

    /**
     * @param enemy   the enemy instance to spawn
     * @param targetX the X position the enemy moves to and stops at
     * @param targetY the Y position the enemy moves to and stops at
     */
    public SpawnEntry(Enemy enemy, float targetX, float targetY) {
        this.enemy = enemy;
        this.targetX = targetX;
        this.targetY = targetY;
    }
}
