package com.github.ghomerl.chimken.model.levels;

import com.badlogic.gdx.utils.Array;


public class Wave {

    // How the enemies in this wave move after spawning.
    public enum MovementType {
        FORMATION,
        SNAKE,
        ZIGZAG
    }

    private final Array<SpawnEntry> spawns;
    private MovementType movementType = MovementType.FORMATION;

    public Wave() {
        this.spawns = new Array<>();
    }


    public Wave add(SpawnEntry entry) {
        spawns.add(entry);
        return this;
    }


    public Array<SpawnEntry> getSpawns() {
        return spawns;
    }


    public int size() {
        return spawns.size;
    }



    public MovementType getMovementType() {
        return movementType;
    }

    public Wave setMovementType(MovementType movementType) {
        this.movementType = movementType;
        return this;
    }
}
