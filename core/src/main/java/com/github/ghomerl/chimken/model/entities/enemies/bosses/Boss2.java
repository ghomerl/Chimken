package com.github.ghomerl.chimken.model.entities.enemies.bosses;

import com.github.ghomerl.chimken.model.entities.enemies.enums.BossType;


public class Boss2 extends Boss {

    public static final float DEFAULT_WIDTH = 300f;
    public static final float DEFAULT_HEIGHT = 300f;
    public static final float DEFAULT_SPEED = 200f;
    public static final int DEFAULT_HP = 5000;
    public static final int DEFAULT_DAMAGE = 1;
    public static final int DEFAULT_POINTS = 20_000;


    public Boss2(float x, float y) {
        super(BossType.BOSS_2, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT,
              DEFAULT_SPEED, DEFAULT_HP, DEFAULT_DAMAGE, DEFAULT_POINTS);
    }
}
