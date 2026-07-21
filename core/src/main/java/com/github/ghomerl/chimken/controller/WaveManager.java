package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.levels.SpawnEntry;
import com.github.ghomerl.chimken.model.levels.Wave;


public class WaveManager {


    private final Array<Wave> waves;


    private int currentWaveIndex;


    private final int lastWaveIndex;


    private boolean waveCleared;


    public WaveManager(Array<Wave> waves, int lastWaveIndex) {
        this.waves = waves;
        this.lastWaveIndex = lastWaveIndex;
        this.currentWaveIndex = 0;
        this.waveCleared = false;
    }


    public void spawnCurrentWave(Array<Enemy> liveEnemies) {
        if (currentWaveIndex >= waves.size) return;

        Wave wave = waves.get(currentWaveIndex);
        for (SpawnEntry entry : wave.getSpawns()) {
            liveEnemies.add(entry.enemy());
        }
        waveCleared = false;
    }


    public Array<SpawnEntry> getCurrentSpawns() {
        if (currentWaveIndex >= waves.size) return new Array<SpawnEntry>();
        return waves.get(currentWaveIndex).getSpawns();
    }


    public boolean isCurrentWaveCleared() {
        if (waveCleared) return true;
        if (currentWaveIndex >= waves.size) return true;

        Wave wave = waves.get(currentWaveIndex);
        for (SpawnEntry entry : wave.getSpawns()) {
            if (entry.enemy().isAlive()) return false;
        }
        waveCleared = true;
        return true;
    }


    public boolean advanceWave() {
        currentWaveIndex++;
        return currentWaveIndex <= lastWaveIndex;
    }


    public boolean wasLastWave() {
        return currentWaveIndex >= lastWaveIndex && waveCleared;
    }


    public int getCurrentWaveNumber() {
        return currentWaveIndex + 1;
    }


    public int getTotalWaves() {
        return waves.size;
    }


    public Wave getCurrentWave() {
        if (currentWaveIndex >= waves.size) return null;
        return waves.get(currentWaveIndex);
    }
}
