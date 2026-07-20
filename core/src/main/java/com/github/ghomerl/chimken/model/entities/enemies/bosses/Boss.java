package com.github.ghomerl.chimken.model.entities.enemies.bosses;

import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.enums.BossType;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;

/**
 * Abstract base class for all boss enemies.
 * <p>
 * Bosses are distinct from regular enemies in that they typically
 * have multiple attack phases, higher health pools, unique movement
 * patterns, and special drop rewards. Every concrete boss must
 * declare its {@link BossType} so the level-design system can
 * reference it.
 */
public abstract class Boss extends Enemy {

    private final BossType bossType;

    /**
     * Creates a new boss with the given type and stats.
     *
     * @param bossType the boss-type enum entry
     * @param x        horizontal position
     * @param y        vertical position
     * @param width    hit-box / render width
     * @param height   hit-box / render height
     * @param speed    movement speed (pixels / second)
     * @param hp       starting (and maximum) hit points
     * @param damage   contact damage dealt to the player on collision
     * @param points   score awarded when defeated
     */
    protected Boss(BossType bossType, float x, float y, float width, float height,
                   float speed, int hp, int damage, int points) {
        super(x, y, width, height, speed, hp, damage, points);
        this.bossType = bossType;
    }

    /**
     * Returns the {@link BossType} that identifies this boss variant.
     */
    public BossType getBossType() {
        return bossType;
    }
}
