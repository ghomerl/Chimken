package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.items.*;
import com.github.ghomerl.chimken.model.entities.weapons.WeaponFactory;
import com.github.ghomerl.chimken.model.entities.weapons.WeaponType;

import java.util.Random;


public final class DropController {

    private static final Random random = new Random();

    private DropController() {  }

    public static void rollDrops(Enemy enemy, Player player,
                                 Array<Item> items, float worldWidth) {
        float cx = enemy.getX() + enemy.getWidth() / 2f;
        float cy = enemy.getY() + enemy.getHeight() / 2f;
        float throwSpeed = player.getWeapon().getProjectileSpeed() * 2f / 3f;
        float angle = random.nextBoolean() ? 45f : -45f;
        double rad = Math.toRadians(angle);
        float vx = (float) (throwSpeed * Math.cos(rad));
        float vy = (float) (throwSpeed * Math.sin(rad));


        if (random.nextFloat() < 0.25f) {
            float roll = random.nextFloat();
            BouncingItem food;
            if (roll < 0.5f) {
                food = new Drumstick(cx, cy, vx, vy, worldWidth);
            } else if (roll < 0.9f) {
                food = new Burger(cx, cy, vx, vy, worldWidth);
            } else {
                food = new Roast(cx, cy, vx, vy, worldWidth);
            }
            items.add(food);
        }

        else if (random.nextInt(25) == 0) {
            items.add(new KeyItem(cx, cy, vx, vy, worldWidth));
        }


        if (random.nextInt(10) == 0) {
            if (random.nextBoolean()) {
                items.add(new PowerUpItem(cx, cy));
            } else {
                WeaponType[] types = WeaponFactory.allTypes();
                WeaponType type = types[random.nextInt(types.length)];
                items.add(new GiftItem(cx, cy, type));
            }
        }
    }
}
