package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.Enemy;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;

/**
 * Static utility that performs AABB overlap checks for the three
 * collision types present in the game.
 */
public final class CollisionController {

    private CollisionController() {
    }

    /**
     * Returns {@code true} when the player is in a state where
     * collisions should be processed (visible and not invincible).
     */
    public static boolean canPlayerCollide(Player player) {
        return player.isVisible() && !player.isInvincible();
    }

    // ── Player body ↔ Enemy body ──────────────────────────────────

    /**
     * Checks whether the player's hitbox overlaps an enemy's hitbox.
     */
    public static boolean checkPlayerEnemyCollision(Player player, Enemy enemy) {
        if (!canPlayerCollide(player) || !enemy.isAlive()) {
            return false;
        }
        return player.getHitbox().overlaps(enemy.getHitbox());
    }

    // ── Projectile (egg) ↔ Player ────────────────────────────────

    /**
     * Checks whether a projectile (enemy egg) overlaps the player.
     */
    public static boolean checkProjectilePlayerCollision(Projectile projectile, Player player) {
        if (!projectile.isActive() || !canPlayerCollide(player)) {
            return false;
        }
        return projectile.getHitbox().overlaps(player.getHitbox());
    }

    // ── Projectile (plasma) ↔ Enemy ──────────────────────────────

    /**
     * Checks whether a projectile (player's plasma bolt) overlaps an enemy.
     */
    public static boolean checkProjectileEnemyCollision(Projectile projectile, Enemy enemy) {
        if (!projectile.isActive() || !enemy.isAlive()) {
            return false;
        }
        return projectile.getHitbox().overlaps(enemy.getHitbox());
    }
}
