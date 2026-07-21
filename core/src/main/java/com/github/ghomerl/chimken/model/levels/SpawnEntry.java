package com.github.ghomerl.chimken.model.levels;

import com.github.ghomerl.chimken.model.entities.enemies.Enemy;


public record SpawnEntry(Enemy enemy, float targetX, float targetY) {

}
