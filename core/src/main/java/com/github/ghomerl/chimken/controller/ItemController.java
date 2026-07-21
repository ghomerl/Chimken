package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.controller.audio.SfxManager;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.items.*;
import com.github.ghomerl.chimken.model.entities.weapons.WeaponFactory;
import com.github.ghomerl.chimken.model.entities.weapons.enums.WeaponType;
import com.badlogic.gdx.utils.Array;


public final class ItemController {

    private ItemController() {
    }


    public static void update(Array<Item> items, Player player, float delta) {
        for (int i = items.size - 1; i >= 0; i--) {
            Item item = items.get(i);
            item.update(delta);

            if (!item.isActive()) {
                items.removeIndex(i);
                continue;
            }

            if (canPlayerCollect(player, item)) {
                collectItem(player, item);
                item.setActive(false);
                items.removeIndex(i);
            }
        }
    }

    private static boolean canPlayerCollect(Player player, Item item) {
        if (!player.isVisible() || player.isInvincible()) return false;
        return player.getHitbox().overlaps(item.getHitbox());
    }

    private static void collectItem(Player player, Item item) {
        player.setTotalPoints(player.getTotalPoints() + item.getPoints());

        if (item.getFoodUnits() > 0) {
            int oldFood = player.getFoodObtained();
            player.setFoodObtained(oldFood + item.getFoodUnits());
            int newFood = player.getFoodObtained();

            // Grant 1 missile for every 50 food accumulated
            int missilesGained = (newFood / 50) - (oldFood / 50);
            if (missilesGained > 0) {
                player.setMissileCount(player.getMissileCount() + missilesGained);
            }
        }

        if (item.getKeyCount() > 0) {
            player.setKeysObtained(player.getKeysObtained() + item.getKeyCount());
        }

        if (item instanceof PowerUpItem) {
            player.setWeaponLevel(player.getWeaponLevel() + 1);
            SfxManager.playPowerup();
        } else if (item instanceof GiftItem) {
            WeaponType type = ((GiftItem) item).getWeaponType();
            if (player.getWeapon().getWeaponType() == type) {
                player.setWeaponLevel(player.getWeaponLevel() + 1);
            } else {
                player.setWeapon(WeaponFactory.create(type));
            }
            SfxManager.playGift();
        }
    }
}
