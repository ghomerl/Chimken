package com.github.ghomerl.chimken.model.entities.weapons;

import com.github.ghomerl.chimken.model.entities.weapons.enums.WeaponType;

public final class WeaponFactory {

    private WeaponFactory() {
    }


    public static Weapon create(WeaponType type) {
        switch (type) {
            case BORON_RAILGUN:
                return new BoronRailgun();
            case PLASMA_BLASTER:
            default:
                return new PlasmaBlaster();
        }
    }


    public static WeaponType[] allTypes() {
        return WeaponType.values();
    }
}
