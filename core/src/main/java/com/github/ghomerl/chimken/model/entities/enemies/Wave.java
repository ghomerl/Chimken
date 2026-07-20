package com.github.ghomerl.chimken.model.entities.enemies;

import com.badlogic.gdx.utils.Array;

/**
 * A single wave of enemies.  Contains an ordered list of
 * {@link SpawnEntry} instances that are spawned simultaneously
 * when the wave begins.
 * <p>
 * A wave is considered complete when every enemy it spawned
 * is no longer alive.
 */
public class Wave {

    private final Array<SpawnEntry> spawns;

    public Wave() {
        this.spawns = new Array<>();
    }

    /**
     * Adds an enemy spawn to this wave.
     */
    public Wave add(SpawnEntry entry) {
        spawns.add(entry);
        return this;
    }

    /**
     * @return all spawn entries in this wave (order matters for indexing)
     */
    public Array<SpawnEntry> getSpawns() {
        return spawns;
    }

    /**
     * @return the number of enemies in this wave
     */
    public int size() {
        return spawns.size;
    }
}
