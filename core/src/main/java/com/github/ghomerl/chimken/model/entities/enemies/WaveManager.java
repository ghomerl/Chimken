package com.github.ghomerl.chimken.model.entities.enemies;

import com.badlogic.gdx.utils.Array;

/**
 * Manages the progression through a sequence of {@link Wave}s.
 * <p>
 * The manager owns the list of all waves for the current level.
 * It tracks the current wave index, spawns enemies into the
 * provided live-enemy array, and detects when the current wave
 * is cleared so the next one can begin (or the level is won).
 */
public class WaveManager {

    /** The waves that compose this level, in order. */
    private final Array<Wave> waves;

    /** Index of the wave currently being played (0-based). */
    private int currentWaveIndex;

    /** The last wave index — when this wave is cleared, the level is won. */
    private final int lastWaveIndex;

    /** {@code true} once all enemies of the current wave have been killed. */
    private boolean waveCleared;

    /**
     * Creates a wave manager for the given level definition.
     *
     * @param waves         all waves of the level, in order
     * @param lastWaveIndex the index of the final wave
     */
    public WaveManager(Array<Wave> waves, int lastWaveIndex) {
        this.waves = waves;
        this.lastWaveIndex = lastWaveIndex;
        this.currentWaveIndex = 0;
        this.waveCleared = false;
    }

    // ── Spawning ──────────────────────────────────────────────────

    /**
     * Spawns all enemies of the current wave into the provided array
     * and resets the wave-cleared flag.
     *
     * @param liveEnemies the game screen's live enemy list
     */
    public void spawnCurrentWave(Array<Enemy> liveEnemies) {
        if (currentWaveIndex >= waves.size) return;

        Wave wave = waves.get(currentWaveIndex);
        for (SpawnEntry entry : wave.getSpawns()) {
            liveEnemies.add(entry.enemy);
        }
        waveCleared = false;
    }

    /**
     * Returns the spawn entries of the current wave.
     * Used by the movement controller to know each enemy's target.
     */
    public Array<SpawnEntry> getCurrentSpawns() {
        if (currentWaveIndex >= waves.size) return new Array<SpawnEntry>();
        return waves.get(currentWaveIndex).getSpawns();
    }

    // ── State queries ─────────────────────────────────────────────

    /**
     * Checks whether every enemy spawned by the current wave is dead.
     * When true the caller should transition to the next wave
     * or trigger the win condition.
     */
    public boolean isCurrentWaveCleared() {
        if (waveCleared) return true;
        if (currentWaveIndex >= waves.size) return true;

        Wave wave = waves.get(currentWaveIndex);
        for (SpawnEntry entry : wave.getSpawns()) {
            if (entry.enemy.isAlive()) return false;
        }
        waveCleared = true;
        return true;
    }

    /**
     * Advances to the next wave.  Returns {@code false} if there
     * are no more waves after the current one.
     */
    public boolean advanceWave() {
        currentWaveIndex++;
        return currentWaveIndex <= lastWaveIndex;
    }

    /** @return {@code true} if the most recently cleared wave was the last wave */
    public boolean wasLastWave() {
        return currentWaveIndex >= lastWaveIndex && waveCleared;
    }

    /** @return 1-based wave number currently being played */
    public int getCurrentWaveNumber() {
        return currentWaveIndex + 1;
    }

    /** @return total number of waves in this level */
    public int getTotalWaves() {
        return waves.size;
    }

    /**
     * Returns the wave object currently being played,
     * or {@code null} if the index is out of range.
     */
    public Wave getCurrentWave() {
        if (currentWaveIndex >= waves.size) return null;
        return waves.get(currentWaveIndex);
    }
}
