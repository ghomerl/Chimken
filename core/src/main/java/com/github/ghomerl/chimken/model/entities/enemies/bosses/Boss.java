package com.github.ghomerl.chimken.model.entities.enemies.bosses;

import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.enums.BossType;

public abstract class Boss extends Enemy {

    private final BossType bossType;


    protected Boss(BossType bossType, float x, float y, float width, float height,
                   float speed, int hp, int damage, int points) {
        super(x, y, width, height, speed, hp, damage, points);
        this.bossType = bossType;
    }


    public BossType getBossType() {
        return bossType;
    }
}
