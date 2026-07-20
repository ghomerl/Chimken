package com.github.ghomerl.chimken.model.entities.items;

import com.github.ghomerl.chimken.model.entities.weapons.enums.WeaponType;

public class GiftItem extends FallingItem {
    private static final float WIDTH = 28f;
    private static final float HEIGHT = 28f;
    private static final int POINTS = 1000;

    private final WeaponType weaponType;

    public GiftItem(float x, float y, WeaponType weaponType) {
        super(x, y, WIDTH, HEIGHT, POINTS);
        this.weaponType = weaponType;
    }

    public WeaponType getWeaponType() { return weaponType; }
}
