package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;


public final class CollisionController {

    private CollisionController() {
    }


    public static boolean canPlayerCollide(Player player) {
        return player.isVisible() && !player.isInvincible();
    }


    public static boolean checkPlayerEnemyCollision(Player player, Enemy enemy) {
        if (!canPlayerCollide(player) || !enemy.isAlive()) {
            return false;
        }
        return player.getHitbox().overlaps(enemy.getHitbox());
    }


    public static boolean checkProjectilePlayerCollision(Projectile projectile, Player player) {
        if (!projectile.isActive() || !canPlayerCollide(player)) {
            return false;
        }
        return projectile.getHitbox().overlaps(player.getHitbox());
    }


    public static boolean checkProjectileEnemyCollision(Projectile projectile, Enemy enemy) {
        if (!projectile.isActive() || !enemy.isAlive()) {
            return false;
        }
        return projectile.getHitbox().overlaps(enemy.getHitbox());
    }
}
